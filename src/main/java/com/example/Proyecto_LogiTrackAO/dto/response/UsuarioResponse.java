package com.example.Proyecto_LogiTrackAO.dto.response;

public record UsuarioResponse(
        Long id,
        String username,
        String email,
        String rolNombre
) {}