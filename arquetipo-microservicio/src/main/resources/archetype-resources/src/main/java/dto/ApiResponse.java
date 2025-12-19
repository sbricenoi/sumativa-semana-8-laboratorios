package ${package}.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Respuesta estándar para todas las APIs del sistema
 * Incluye traceId para trazabilidad, code, message y data
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
     * Constructor para respuestas con código personalizado
     */
    public static <T> ApiResponse<T> custom(String code, String message, T data) {
        return new ApiResponse<>(
            UUID.randomUUID().toString(),
            code,
            message,
            data
        );
    }
}

