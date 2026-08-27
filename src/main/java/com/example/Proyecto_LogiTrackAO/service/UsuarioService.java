// service/UsuarioService.java
package com.example.Proyecto_LogiTrackAO.service;

import com.example.Proyecto_LogiTrackAO.dto.request.UsuarioRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse crearUsuario(UsuarioRequest request);
    UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request);
    void eliminarUsuario(Long id);
    List<UsuarioResponse> obtenerUsuarios();
    UsuarioResponse buscarPorId(Long id);
}