// mapper/MovimientoMapper.java
package com.example.Proyecto_LogiTrackAO.mapper;

import com.example.Proyecto_LogiTrackAO.dto.response.DetalleItemResponse;
import com.example.Proyecto_LogiTrackAO.dto.response.MovimientoResponse;
import com.example.Proyecto_LogiTrackAO.model.DetalleMovimiento;
import com.example.Proyecto_LogiTrackAO.model.Movimiento;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovimientoMapper {

    public MovimientoResponse entityToDto(Movimiento movimiento, List<DetalleMovimiento> detalles) {
        List<DetalleItemResponse> detallesDto = detalles.stream()
                .map(d -> new DetalleItemResponse(
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getCantidad()
                ))
                .toList();

        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getFechaHora(),
                movimiento.getTipo(),
                movimiento.getUsuario().getUsername(),
                movimiento.getBodegaOrigen() != null ? movimiento.getBodegaOrigen().getNombre() : null,
                movimiento.getBodegaDestino() != null ? movimiento.getBodegaDestino().getNombre() : null,
                detallesDto
        );
    }
}