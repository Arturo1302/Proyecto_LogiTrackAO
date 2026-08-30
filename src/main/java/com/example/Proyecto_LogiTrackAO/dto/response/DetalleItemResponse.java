// dto/response/DetalleItemResponse.java
package com.example.Proyecto_LogiTrackAO.dto.response;

public record DetalleItemResponse(
        Long productoId,
        String productoNombre,
        int cantidad
) {}