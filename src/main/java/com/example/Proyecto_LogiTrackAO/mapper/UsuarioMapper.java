package com.example.Proyecto_LogiTrackAO.mapper;

import com.example.Proyecto_LogiTrackAO.dto.request.UsuarioRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.UsuarioResponse;
import com.example.Proyecto_LogiTrackAO.model.Rol;
import com.example.Proyecto_LogiTrackAO.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario dtoToEntity(UsuarioRequest request, Rol rol) {
        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setEmail(request.email());
        usuario.setPassword(request.password());
        usuario.setRol(rol);
        return usuario;
    }

    public UsuarioResponse entityToDto(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRol().getNombre()
        );
    }
}