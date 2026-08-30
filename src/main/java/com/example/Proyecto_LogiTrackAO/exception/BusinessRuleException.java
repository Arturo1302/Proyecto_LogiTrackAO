// exception/BusinessRuleException.java
package com.example.Proyecto_LogiTrackAO.exception;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String mensaje) {
        super(mensaje);
    }
}