package com.example.Proyecto_LogiTrackAO.service.impl;

import com.example.Proyecto_LogiTrackAO.dto.request.RolRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.RolResponse;
import com.example.Proyecto_LogiTrackAO.exception.ResourceNotFoundException;
import com.example.Proyecto_LogiTrackAO.mapper.RolMapper;
import com.example.Proyecto_LogiTrackAO.model.Rol;
import com.example.Proyecto_LogiTrackAO.repository.RolRepository;
import com.example.Proyecto_LogiTrackAO.service.RolService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Override
    public RolResponse crear(RolRequest request) {
        Rol rol = rolMapper.dtoToEntity(request);
        Rol guardado = rolRepository.save(rol);
        return rolMapper.entityToDto(guardado);
    }

    @Override
    public RolResponse actualizar(Long id, RolRequest request) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        rol.setNombre(request.nombre());
        Rol actualizado = rolRepository.save(rol);
        return rolMapper.entityToDto(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rol no encontrado con id: " + id);
        }
        rolRepository.deleteById(id);
    }

    @Override
    public List<RolResponse> listarTodos() {
        return rolRepository.findAll().stream()
                .map(rolMapper::entityToDto)
                .toList();
    }

    @Override
    public RolResponse buscarPorId(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        return rolMapper.entityToDto(rol);
    }
}