// service/impl/AuditoriaServiceImpl.java
package com.example.Proyecto_LogiTrackAO.service.impl;

import com.example.Proyecto_LogiTrackAO.dto.response.AuditoriaResponse;
import com.example.Proyecto_LogiTrackAO.mapper.AuditoriaMapper;
import com.example.Proyecto_LogiTrackAO.model.Auditoria;
import com.example.Proyecto_LogiTrackAO.model.TipoOperacion;
import com.example.Proyecto_LogiTrackAO.repository.AuditoriaRepository;
import com.example.Proyecto_LogiTrackAO.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;

    @Override
    public List<AuditoriaResponse> obtenerTodas() {
        return auditoriaRepository.findAll().stream()
                .map(auditoriaMapper::entityToDto)
                .toList();
    }

    @Override
    public AuditoriaResponse buscarPorId(Long id) {
        Auditoria auditoria = auditoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auditoría no encontrada con id: " + id));
        return auditoriaMapper.entityToDto(auditoria);
    }

    // service/impl/AuditoriaServiceImpl.java — agrega
    @Override
    public List<AuditoriaResponse> obtenerPorUsuario(String usuario) {
        return auditoriaRepository.findByUsuario(usuario).stream()
                .map(auditoriaMapper::entityToDto)
                .toList();
    }

    @Override
    public List<AuditoriaResponse> obtenerPorOperacion(TipoOperacion operacion) {
        return auditoriaRepository.findByOperacion(operacion).stream()
                .map(auditoriaMapper::entityToDto)
                .toList();
    }
}