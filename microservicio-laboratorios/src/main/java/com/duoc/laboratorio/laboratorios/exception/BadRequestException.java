package com.duoc.laboratorio.laboratorios.exception;

/**
 * Excepción personalizada para peticiones incorrectas
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}


