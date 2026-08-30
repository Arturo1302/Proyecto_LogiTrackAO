// dto/request/DetalleItemRequest.java
package com.example.Proyecto_LogiTrackAO.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DetalleItemRequest(
        @NotNull Long productoId,
        @NotNull @Min(1) Integer cantidad
) {}