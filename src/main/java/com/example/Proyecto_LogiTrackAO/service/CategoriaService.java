package com.example.Proyecto_LogiTrackAO.service;

import com.example.Proyecto_LogiTrackAO.dto.request.CategoriaRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.CategoriaResponse;

import java.util.List;

public interface CategoriaService {

    CategoriaResponse crearCategoria(CategoriaRequest categoriaRequest);
    CategoriaResponse actualizarCategoria(Long id, CategoriaRequest categoriaRequest);
    CategoriaResponse eliminarCategoria(Long id);
    List<CategoriaResponse> obtenerCategorias();
    CategoriaResponse buscarPorId(Long id);
}
