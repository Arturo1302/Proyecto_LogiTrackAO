package com.example.Proyecto_LogiTrackAO.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RolRequest(
        @NotBlank(message = "El nombre del rol es obligatorio")
        @Size(max = 20, message = "El nombre no puede superar 20 caracteres")
        String nombre
) {}