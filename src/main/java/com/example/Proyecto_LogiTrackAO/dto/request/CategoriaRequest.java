package com.example.Proyecto_LogiTrackAO.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(

        @NotBlank(message = "El nombre de la categoria es obligatorio")
        @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
        String nombre,

        @Size(max = 150, message = "La descripcion no puede pasar los 150 caracteres")
        String descripcion

) {

}
