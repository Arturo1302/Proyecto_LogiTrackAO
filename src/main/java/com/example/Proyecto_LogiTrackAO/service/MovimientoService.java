// service/MovimientoService.java
package com.example.Proyecto_LogiTrackAO.service;

import com.example.Proyecto_LogiTrackAO.dto.request.MovimientoRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.MovimientoResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {
    MovimientoResponse registrarMovimiento(MovimientoRequest request);
    List<MovimientoResponse> obtenerTodos();
    MovimientoResponse buscarPorId(Long id);
    List<MovimientoResponse> obtenerPorRangoFechas(LocalDateTime inicio, LocalDateTime fin);
}