// mapper/BodegaMapper.java
package com.example.Proyecto_LogiTrackAO.mapper;

import com.example.Proyecto_LogiTrackAO.dto.request.BodegaRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.BodegaResponse;
import com.example.Proyecto_LogiTrackAO.model.Bodega;
import com.example.Proyecto_LogiTrackAO.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class BodegaMapper {

    public Bodega dtoToEntity(BodegaRequest request, Usuario encargado) {
        Bodega bodega = new Bodega();
        bodega.setNombre(request.nombre());
        bodega.setUbicacion(request.ubicacion());
        bodega.setCapacidad(request.capacidad());
        bodega.setEncargado(encargado); // puede ser null, está permitido
        return bodega;
    }

    public BodegaResponse entityToDto(Bodega bodega) {
        String encargadoUsername = bodega.getEncargado() != null
                ? bodega.getEncargado().getUsername()
                : null;
        return new BodegaResponse(
                bodega.getId(),
                bodega.getNombre(),
                bodega.getUbicacion(),
                bodega.getCapacidad(),
                encargadoUsername
        );
    }
}