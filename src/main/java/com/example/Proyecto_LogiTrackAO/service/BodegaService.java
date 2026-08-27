// service/BodegaService.java
package com.example.Proyecto_LogiTrackAO.service;

import com.example.Proyecto_LogiTrackAO.dto.request.BodegaRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.BodegaResponse;

import java.util.List;

public interface BodegaService {
    BodegaResponse crearBodega(BodegaRequest request);
    BodegaResponse actualizarBodega(Long id, BodegaRequest request);
    void eliminarBodega(Long id);
    List<BodegaResponse> obtenerBodegas();
    BodegaResponse buscarPorId(Long id);
}