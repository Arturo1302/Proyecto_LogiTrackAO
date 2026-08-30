// dto/response/StockPorBodegaResponse.java
package com.example.Proyecto_LogiTrackAO.dto.response;

public record StockPorBodegaResponse(
        Long bodegaId,
        String bodegaNombre,
        Long stockTotal
) {}