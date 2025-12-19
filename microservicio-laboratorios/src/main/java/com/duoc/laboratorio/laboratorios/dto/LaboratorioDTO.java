package com.duoc.laboratorio.laboratorios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferencia de datos de Laboratorio
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratorioDTO {

    private Long idLaboratorio;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    private String especialidad;

    private Integer activo;

    private LocalDateTime fechaCreacion;
}


