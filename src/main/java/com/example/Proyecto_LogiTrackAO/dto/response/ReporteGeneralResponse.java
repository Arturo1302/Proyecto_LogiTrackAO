// dto/response/ReporteGeneralResponse.java
package com.example.Proyecto_LogiTrackAO.dto.response;

import java.util.List;

public record ReporteGeneralResponse(
        List<StockPorBodegaResponse> stockPorBodega,
        List<ProductoMasMovidoResponse> productosMasMovidos
) {}