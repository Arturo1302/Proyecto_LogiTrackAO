package com.example.Proyecto_LogiTrackAO.service.impl;

import com.example.Proyecto_LogiTrackAO.dto.request.CategoriaRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.CategoriaResponse;
import com.example.Proyecto_LogiTrackAO.mapper.CategoriaMapper;
import com.example.Proyecto_LogiTrackAO.model.Categoria;
import com.example.Proyecto_LogiTrackAO.repository.CategoriaRepository;
import com.example.Proyecto_LogiTrackAO.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    public CategoriaResponse crearCategoria(CategoriaRequest categoriaRequest) {
        Categoria categoria = categoriaMapper.dtoToEntity(categoriaRequest);
        Categoria guardada = categoriaRepository.save(categoria);
        return categoriaMapper.entityToDto(guardada);
    }

    @Override
    public CategoriaResponse actualizarCategoria(Long id, CategoriaRequest categoriaRequest) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
        categoria.setNombre(categoriaRequest.nombre());
        categoria.setDescripcion(categoriaRequest.descripcion());
        Categoria actualizada = categoriaRepository.save(categoria);
        return categoriaMapper.entityToDto(actualizada);
    }

    @Override
    public CategoriaResponse eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con id: " + id);
        }
        categoriaRepository.deleteById(id);
        return null;
    }

    @Override
    public List<CategoriaResponse> obtenerCategorias() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::entityToDto)
                .toList();
    }

    @Override
    public CategoriaResponse buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
        return categoriaMapper.entityToDto(categoria);
    }
}