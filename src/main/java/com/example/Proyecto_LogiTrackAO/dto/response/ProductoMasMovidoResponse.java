// dto/response/ProductoMasMovidoResponse.java
package com.example.Proyecto_LogiTrackAO.dto.response;

public record ProductoMasMovidoResponse(
        Long productoId,
        String productoNombre,
        Long cantidadTotal
) {}