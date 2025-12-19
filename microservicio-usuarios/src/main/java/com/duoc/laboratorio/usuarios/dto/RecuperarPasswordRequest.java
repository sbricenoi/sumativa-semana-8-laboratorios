package com.duoc.laboratorio.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitud de recuperación de contraseña
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecuperarPasswordRequest {
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;
}


