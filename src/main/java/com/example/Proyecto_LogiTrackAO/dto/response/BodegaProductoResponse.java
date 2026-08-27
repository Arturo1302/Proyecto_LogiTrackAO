// dto/response/BodegaProductoResponse.java
package com.example.Proyecto_LogiTrackAO.dto.response;

public record BodegaProductoResponse(
        Long bodegaId,
        String bodegaNombre,
        Long productoId,
        String productoNombre,
        int stock
) {}