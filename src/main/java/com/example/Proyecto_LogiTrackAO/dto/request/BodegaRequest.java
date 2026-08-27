package com.example.Proyecto_LogiTrackAO.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BodegaRequest(
        @NotBlank @Size(max = 100)
        String nombre,

        @NotBlank @Size(max = 150)
        String ubicacion,

        @NotNull @Min(0)
        Integer capacidad,

        Long encargadoId // opcional, puede venir null
) {}