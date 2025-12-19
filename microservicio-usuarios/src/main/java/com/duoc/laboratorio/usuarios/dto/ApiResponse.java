package com.duoc.laboratorio.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String traceId;
    private String code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(UUID.randomUUID().toString(), "SUCCESS", message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(UUID.randomUUID().toString(), "ERROR", message, null);
    }

    public static <T> ApiResponse<T> error(String traceId, String message) {
        return new ApiResponse<>(traceId, "ERROR", message, null);
    }
}
