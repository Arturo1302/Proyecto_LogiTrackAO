package com.example.Proyecto_LogiTrackAO.mapper;

import com.example.Proyecto_LogiTrackAO.dto.request.RolRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.RolResponse;
import com.example.Proyecto_LogiTrackAO.model.Rol;
import org.springframework.stereotype.Component;

@Component
public class RolMapper {

    public Rol dtoToEntity(RolRequest request) {
        Rol rol = new Rol();
        rol.setNombre(request.nombre());
        return rol;
    }

    public RolResponse entityToDto(Rol rol) {

        return new RolResponse(rol.getId(), rol.getNombre());
    }
}