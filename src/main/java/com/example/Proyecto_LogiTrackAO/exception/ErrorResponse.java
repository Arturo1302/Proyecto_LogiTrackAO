package com.example.Proyecto_LogiTrackAO.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String message,
        String errorCode
) {}