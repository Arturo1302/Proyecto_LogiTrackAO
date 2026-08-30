// exception/ResourceNotFoundException.java
package com.example.Proyecto_LogiTrackAO.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}