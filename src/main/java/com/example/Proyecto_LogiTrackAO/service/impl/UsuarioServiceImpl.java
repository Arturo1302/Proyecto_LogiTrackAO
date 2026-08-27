// service/impl/UsuarioServiceImpl.java
package com.example.Proyecto_LogiTrackAO.service.impl;

import com.example.Proyecto_LogiTrackAO.dto.request.UsuarioRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.UsuarioResponse;
import com.example.Proyecto_LogiTrackAO.mapper.UsuarioMapper;
import com.example.Proyecto_LogiTrackAO.model.Rol;
import com.example.Proyecto_LogiTrackAO.model.Usuario;
import com.example.Proyecto_LogiTrackAO.repository.RolRepository;
import com.example.Proyecto_LogiTrackAO.repository.UsuarioRepository;
import com.example.Proyecto_LogiTrackAO.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        Rol rol = rolRepository.findById(request.rolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + request.rolId()));
        Usuario usuario = usuarioMapper.dtoToEntity(request, rol);
        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.entityToDto(guardado);
    }

    @Override
    public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        Rol rol = rolRepository.findById(request.rolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + request.rolId()));
        usuario.setUsername(request.username());
        usuario.setEmail(request.email());
        usuario.setPassword(request.password());
        usuario.setRol(rol);
        Usuario actualizado = usuarioRepository.save(usuario);
        return usuarioMapper.entityToDto(actualizado);
    }

    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<UsuarioResponse> obtenerUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::entityToDto)
                .toList();
    }

    @Override
    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return usuarioMapper.entityToDto(usuario);
    }
}