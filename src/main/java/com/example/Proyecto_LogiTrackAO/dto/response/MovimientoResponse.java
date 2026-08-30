// dto/response/MovimientoResponse.java
package com.example.Proyecto_LogiTrackAO.dto.response;

import com.example.Proyecto_LogiTrackAO.model.TipoMovimiento;

import java.time.LocalDateTime;
import java.util.List;

public record MovimientoResponse(
        Long id,
        LocalDateTime fechaHora,
        TipoMovimiento tipo,
        String usuarioUsername,
        String bodegaOrigenNombre,   // null si no aplica
        String bodegaDestinoNombre, // null si no aplica
        List<DetalleItemResponse> detalles
) {}