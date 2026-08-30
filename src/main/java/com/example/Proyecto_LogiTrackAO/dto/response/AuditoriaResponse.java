// dto/response/AuditoriaResponse.java
package com.example.Proyecto_LogiTrackAO.dto.response;

import com.example.Proyecto_LogiTrackAO.model.TipoOperacion;

import java.time.LocalDateTime;

public record AuditoriaResponse(
        Long id,
        TipoOperacion operacion,
        String entidad,
        Long entidadId,
        LocalDateTime fechaHora,
        String usuario,
        String valoresAnteriores,
        String valoresNuevos
) {}