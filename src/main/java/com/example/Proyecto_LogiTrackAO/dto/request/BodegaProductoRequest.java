// dto/request/BodegaProductoRequest.java
package com.example.Proyecto_LogiTrackAO.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BodegaProductoRequest(
        @NotNull(message = "La bodega es obligatoria")
        Long bodegaId,

        @NotNull(message = "El producto es obligatorio")
        Long productoId,

        @NotNull @Min(0)
        Integer stock
) {}