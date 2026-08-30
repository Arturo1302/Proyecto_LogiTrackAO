// dto/request/MovimientoRequest.java
package com.example.Proyecto_LogiTrackAO.dto.request;

import com.example.Proyecto_LogiTrackAO.model.TipoMovimiento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MovimientoRequest(
        @NotNull(message = "El tipo de movimiento es obligatorio")
        TipoMovimiento tipo,

        @NotNull(message = "El usuario responsable es obligatorio")
        Long usuarioId,

        Long bodegaOrigenId,   // obligatorio según el tipo, validado en el service

        Long bodegaDestinoId,  // obligatorio según el tipo, validado en el service

        @NotEmpty(message = "Debe incluir al menos un producto")
        @Valid
        List<DetalleItemRequest> detalles
) {}