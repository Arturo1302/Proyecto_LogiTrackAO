// service/impl/BodegaServiceImpl.java
package com.example.Proyecto_LogiTrackAO.service.impl;

import com.example.Proyecto_LogiTrackAO.dto.request.BodegaRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.BodegaResponse;
import com.example.Proyecto_LogiTrackAO.mapper.BodegaMapper;
import com.example.Proyecto_LogiTrackAO.model.Bodega;
import com.example.Proyecto_LogiTrackAO.model.Usuario;
import com.example.Proyecto_LogiTrackAO.repository.BodegaRepository;
import com.example.Proyecto_LogiTrackAO.repository.UsuarioRepository;
import com.example.Proyecto_LogiTrackAO.service.BodegaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepository bodegaRepository;
    private final UsuarioRepository usuarioRepository;
    private final BodegaMapper bodegaMapper;

    @Override
    public BodegaResponse crearBodega(BodegaRequest request) {
        Usuario encargado = buscarEncargadoSiAplica(request.encargadoId());
        Bodega bodega = bodegaMapper.dtoToEntity(request, encargado);
        Bodega guardada = bodegaRepository.save(bodega);
        return bodegaMapper.entityToDto(guardada);
    }

    @Override
    public BodegaResponse actualizarBodega(Long id, BodegaRequest request) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada con id: " + id));
        Usuario encargado = buscarEncargadoSiAplica(request.encargadoId());
        bodega.setNombre(request.nombre());
        bodega.setUbicacion(request.ubicacion());
        bodega.setCapacidad(request.capacidad());
        bodega.setEncargado(encargado);
        Bodega actualizada = bodegaRepository.save(bodega);
        return bodegaMapper.entityToDto(actualizada);
    }

    @Override
    public void eliminarBodega(Long id) {
        if (!bodegaRepository.existsById(id)) {
            throw new RuntimeException("Bodega no encontrada con id: " + id);
        }
        bodegaRepository.deleteById(id);
    }

    @Override
    public List<BodegaResponse> obtenerBodegas() {
        return bodegaRepository.findAll().stream()
                .map(bodegaMapper::entityToDto)
                .toList();
    }

    @Override
    public BodegaResponse buscarPorId(Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada con id: " + id));
        return bodegaMapper.entityToDto(bodega);
    }

    private Usuario buscarEncargadoSiAplica(Long encargadoId) {
        if (encargadoId == null) {
            return null;
        }
        return usuarioRepository.findById(encargadoId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + encargadoId));
    }
}