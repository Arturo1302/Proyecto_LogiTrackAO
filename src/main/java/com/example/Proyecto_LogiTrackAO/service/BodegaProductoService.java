// service/BodegaProductoService.java
package com.example.Proyecto_LogiTrackAO.service;

import com.example.Proyecto_LogiTrackAO.dto.request.BodegaProductoRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.BodegaProductoResponse;

import java.util.List;

public interface BodegaProductoService {
    BodegaProductoResponse crear(BodegaProductoRequest request);
    BodegaProductoResponse actualizarStock(Long bodegaId, Long productoId, Integer stock);
    void eliminar(Long bodegaId, Long productoId);
    List<BodegaProductoResponse> obtenerTodos();
    BodegaProductoResponse buscarPorId(Long bodegaId, Long productoId);

    List<BodegaProductoResponse> obtenerStockBajo(int umbral);
}