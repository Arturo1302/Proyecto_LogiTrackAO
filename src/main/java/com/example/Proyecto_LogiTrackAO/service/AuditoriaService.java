// service/AuditoriaService.java
package com.example.Proyecto_LogiTrackAO.service;

import com.example.Proyecto_LogiTrackAO.dto.response.AuditoriaResponse;
import com.example.Proyecto_LogiTrackAO.model.TipoOperacion;

import java.util.List;

public interface AuditoriaService {
    List<AuditoriaResponse> obtenerTodas();
    AuditoriaResponse buscarPorId(Long id);

    List<AuditoriaResponse> obtenerPorUsuario(String usuario);
    List<AuditoriaResponse> obtenerPorOperacion(TipoOperacion operacion);
}