// dto/response/BodegaResponse.java
package com.example.Proyecto_LogiTrackAO.dto.response;

public record BodegaResponse(
        Long id,
        String nombre,
        String ubicacion,
        int capacidad,
        String encargadoUsername // null si no tiene encargado
) {}