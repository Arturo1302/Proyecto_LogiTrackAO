// dto/response/ProductoResponse.java
package com.example.Proyecto_LogiTrackAO.dto.response;

import java.math.BigDecimal;

public record ProductoResponse(
        Long id,
        String nombre,
        String categoriaNombre,
        BigDecimal precio
) {}