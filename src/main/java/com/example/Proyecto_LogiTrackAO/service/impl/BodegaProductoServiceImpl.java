// service/impl/BodegaProductoServiceImpl.java
package com.example.Proyecto_LogiTrackAO.service.impl;

import com.example.Proyecto_LogiTrackAO.dto.request.BodegaProductoRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.BodegaProductoResponse;
import com.example.Proyecto_LogiTrackAO.exception.ResourceNotFoundException;
import com.example.Proyecto_LogiTrackAO.mapper.BodegaProductoMapper;
import com.example.Proyecto_LogiTrackAO.model.Bodega;
import com.example.Proyecto_LogiTrackAO.model.BodegaProducto;
import com.example.Proyecto_LogiTrackAO.model.BodegaProductoId;
import com.example.Proyecto_LogiTrackAO.model.Producto;
import com.example.Proyecto_LogiTrackAO.repository.BodegaProductoRepository;
import com.example.Proyecto_LogiTrackAO.repository.BodegaRepository;
import com.example.Proyecto_LogiTrackAO.repository.ProductoRepository;
import com.example.Proyecto_LogiTrackAO.service.BodegaProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BodegaProductoServiceImpl implements BodegaProductoService {

    private final BodegaProductoRepository bodegaProductoRepository;
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;
    private final BodegaProductoMapper bodegaProductoMapper;


    @Override
    public List<BodegaProductoResponse> obtenerStockBajo(int umbral) {
        return bodegaProductoRepository.findByStockLessThan(umbral).stream()
                .map(bodegaProductoMapper::entityToDto)
                .toList();
    }


    @Override
    public BodegaProductoResponse crear(BodegaProductoRequest request) {
        Bodega bodega = bodegaRepository.findById(request.bodegaId())
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con id: " + request.bodegaId()));
        Producto producto = productoRepository.findById(request.productoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + request.productoId()));
        BodegaProducto bp = bodegaProductoMapper.dtoToEntity(bodega, producto, request.stock());
        BodegaProducto guardado = bodegaProductoRepository.save(bp);
        return bodegaProductoMapper.entityToDto(guardado);
    }

    @Override
    public BodegaProductoResponse actualizarStock(Long bodegaId, Long productoId, Integer stock) {
        BodegaProductoId id = new BodegaProductoId(bodegaId, productoId);
        BodegaProducto bp = bodegaProductoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro no encontrado para esa bodega y producto"));
        bp.setStock(stock);
        BodegaProducto actualizado = bodegaProductoRepository.save(bp);
        return bodegaProductoMapper.entityToDto(actualizado);
    }

    @Override
    public void eliminar(Long bodegaId, Long productoId) {
        BodegaProductoId id = new BodegaProductoId(bodegaId, productoId);
        if (!bodegaProductoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Registro no encontrado para esa bodega y producto");
        }
        bodegaProductoRepository.deleteById(id);
    }

    @Override
    public List<BodegaProductoResponse> obtenerTodos() {
        return bodegaProductoRepository.findAll().stream()
                .map(bodegaProductoMapper::entityToDto)
                .toList();
    }

    @Override
    public BodegaProductoResponse buscarPorId(Long bodegaId, Long productoId) {
        BodegaProductoId id = new BodegaProductoId(bodegaId, productoId);
        BodegaProducto bp = bodegaProductoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro no encontrado para esa bodega y producto"));
        return bodegaProductoMapper.entityToDto(bp);
    }
}