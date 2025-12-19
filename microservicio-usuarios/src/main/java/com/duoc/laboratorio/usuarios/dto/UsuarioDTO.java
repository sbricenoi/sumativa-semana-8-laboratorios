package com.duoc.laboratorio.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long idUsuario;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;
    @Pattern(regexp = "ADMINISTRADOR|PACIENTE|LABORATORISTA|MEDICO", 
             message = "El rol debe ser: ADMINISTRADOR, PACIENTE, LABORATORISTA o MEDICO")
    private String rol;
    private LocalDateTime fechaCreacion;
    private Integer activo;
}
