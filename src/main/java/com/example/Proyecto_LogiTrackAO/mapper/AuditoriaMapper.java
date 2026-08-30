// mapper/AuditoriaMapper.java
package com.example.Proyecto_LogiTrackAO.mapper;

import com.example.Proyecto_LogiTrackAO.dto.response.AuditoriaResponse;
import com.example.Proyecto_LogiTrackAO.model.Auditoria;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {

    public AuditoriaResponse entityToDto(Auditoria auditoria) {
        return new AuditoriaResponse(
                auditoria.getId(),
                auditoria.getOperacion(),
                auditoria.getEntidad(),
                auditoria.getEntidadId(),
                auditoria.getFechaHora(),
                auditoria.getUsuario(),
                auditoria.getValoresAnteriores(),
                auditoria.getValoresNuevos()
        );
    }
}