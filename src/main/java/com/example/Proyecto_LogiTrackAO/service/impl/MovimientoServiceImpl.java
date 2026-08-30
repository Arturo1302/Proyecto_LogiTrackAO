// service/impl/MovimientoServiceImpl.java
package com.example.Proyecto_LogiTrackAO.service.impl;

import com.example.Proyecto_LogiTrackAO.dto.request.DetalleItemRequest;
import com.example.Proyecto_LogiTrackAO.dto.request.MovimientoRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.MovimientoResponse;
import com.example.Proyecto_LogiTrackAO.mapper.MovimientoMapper;
import com.example.Proyecto_LogiTrackAO.model.*;
import com.example.Proyecto_LogiTrackAO.repository.*;
import com.example.Proyecto_LogiTrackAO.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final DetalleMovimientoRepository detalleMovimientoRepository;
    private final BodegaProductoRepository bodegaProductoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoMapper movimientoMapper;

    @Override
    @Transactional
    public MovimientoResponse registrarMovimiento(MovimientoRequest request) {
        validarBodegasSegunTipo(request);

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + request.usuarioId()));

        Bodega bodegaOrigen = request.bodegaOrigenId() != null
                ? bodegaRepository.findById(request.bodegaOrigenId())
                .orElseThrow(() -> new RuntimeException("Bodega origen no encontrada"))
                : null;

        Bodega bodegaDestino = request.bodegaDestinoId() != null
                ? bodegaRepository.findById(request.bodegaDestinoId())
                .orElseThrow(() -> new RuntimeException("Bodega destino no encontrada"))
                : null;

        // Validar stock ANTES de insertar nada, si el tipo lo requiere
        if (request.tipo() == TipoMovimiento.SALIDA || request.tipo() == TipoMovimiento.TRANSFERENCIA) {
            for (DetalleItemRequest item : request.detalles()) {
                validarStockSuficiente(bodegaOrigen.getId(), item.productoId(), item.cantidad());
            }
        }

        // Crear la cabecera del movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(request.tipo());
        movimiento.setUsuario(usuario);
        movimiento.setBodegaOrigen(bodegaOrigen);
        movimiento.setBodegaDestino(bodegaDestino);
        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);

        // Crear cada detalle — cada INSERT dispara el trigger que actualiza el stock
        List<DetalleMovimiento> detallesGuardados = request.detalles().stream()
                .map(item -> {
                    Producto producto = productoRepository.findById(item.productoId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + item.productoId()));
                    DetalleMovimiento detalle = new DetalleMovimiento();
                    detalle.setMovimiento(movimientoGuardado);
                    detalle.setProducto(producto);
                    detalle.setCantidad(item.cantidad());
                    return detalleMovimientoRepository.save(detalle);
                })
                .toList();

        return movimientoMapper.entityToDto(movimientoGuardado, detallesGuardados);
    }

    @Override
    public List<MovimientoResponse> obtenerTodos() {
        return movimientoRepository.findAll().stream()
                .map(m -> movimientoMapper.entityToDto(m, m.getId() != null
                        ? detalleMovimientoRepository.findByMovimientoId(m.getId())
                        : List.of()))
                .toList();
    }

    @Override
    public MovimientoResponse buscarPorId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con id: " + id));
        List<DetalleMovimiento> detalles = detalleMovimientoRepository.findAll().stream()
                .filter(d -> d.getMovimiento().getId().equals(id))
                .toList();
        return movimientoMapper.entityToDto(movimiento, detalles);
    }


    @Override
    public List<MovimientoResponse> obtenerPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoRepository.findByFechaHoraBetween(inicio, fin).stream()
                .map(m -> movimientoMapper.entityToDto(m, detalleMovimientoRepository.findByMovimientoId(m.getId())))
                .toList();
    }

    private void validarBodegasSegunTipo(MovimientoRequest request) {
        switch (request.tipo()) {
            case ENTRADA -> {
                if (request.bodegaDestinoId() == null) {
                    throw new RuntimeException("Una ENTRADA requiere bodega destino");
                }
            }
            case SALIDA -> {
                if (request.bodegaOrigenId() == null) {
                    throw new RuntimeException("Una SALIDA requiere bodega origen");
                }
            }
            case TRANSFERENCIA -> {
                if (request.bodegaOrigenId() == null || request.bodegaDestinoId() == null) {
                    throw new RuntimeException("Una TRANSFERENCIA requiere bodega origen y destino");
                }
            }
        }
    }

    private void validarStockSuficiente(Long bodegaId, Long productoId, int cantidadSolicitada) {
        BodegaProductoId id = new BodegaProductoId(bodegaId, productoId);
        BodegaProducto bp = bodegaProductoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "El producto con id " + productoId + " no tiene stock registrado en esa bodega"));
        if (bp.getStock() < cantidadSolicitada) {
            throw new RuntimeException(
                    "Stock insuficiente para el producto " + productoId +
                            ". Disponible: " + bp.getStock() + ", solicitado: " + cantidadSolicitada);
        }
    }
}