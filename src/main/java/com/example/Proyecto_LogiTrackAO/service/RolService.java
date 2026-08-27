
package com.example.Proyecto_LogiTrackAO.service;

import com.example.Proyecto_LogiTrackAO.dto.request.RolRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.RolResponse;

import java.util.List;

public interface RolService {
    RolResponse crear(RolRequest request);
    RolResponse actualizar(Long id, RolRequest request);
    void eliminar(Long id);
    List<RolResponse> listarTodos();
    RolResponse buscarPorId(Long id);
}