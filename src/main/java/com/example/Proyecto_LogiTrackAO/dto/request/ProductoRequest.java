// dto/request/ProductoRequest.java
package com.example.Proyecto_LogiTrackAO.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoRequest(
        @NotBlank @Size(max = 100)
        String nombre,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId,

        @NotNull @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
        BigDecimal precio
) {}