package com.duoc.laboratorio.laboratorios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Clase DTO para estandarizar las respuestas de la API
 * 
 * Todas las respuestas de los endpoints siguen este formato:
 * - traceId: Identificador único de la petición
 * - code: Código de respuesta (SUCCESS/ERROR)
 * - message: Mensaje descriptivo
 * - data: Datos de respuesta (puede ser null en caso de error)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private String traceId;
    private String code;
    private String message;
    private T data;

    /**
     * Constructor para respuestas exitosas
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
            UUID.randomUUID().toString(),
            "SUCCESS",
            message,
            data
        );
    }

    /**
     * Constructor para respuestas de error
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(
            UUID.randomUUID().toString(),
            "ERROR",
            message,
            null
        );
    }

    /**
     * Constructor para respuestas de error con traceId específico
     */
    public static <T> ApiResponse<T> error(String traceId, String message) {
        return new ApiResponse<>(
            traceId,
            "ERROR",
            message,
            null
        );
    }
}


