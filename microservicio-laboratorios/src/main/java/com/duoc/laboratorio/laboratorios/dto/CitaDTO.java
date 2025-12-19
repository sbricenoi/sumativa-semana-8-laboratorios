package com.duoc.laboratorio.laboratorios.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferencia de datos de Cita
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaDTO {

    private Long idCita;

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long idPaciente;

    @NotNull(message = "El ID del laboratorio es obligatorio")
    private Long idLaboratorio;

    @NotNull(message = "El ID del tipo de análisis es obligatorio")
    private Long idTipoAnalisis;

    @NotNull(message = "La fecha de la cita es obligatoria")
    @Future(message = "La fecha de la cita debe ser futura")
    private LocalDateTime fechaCita;

    @Pattern(regexp = "PROGRAMADA|CONFIRMADA|COMPLETADA|CANCELADA", 
             message = "El estado debe ser: PROGRAMADA, CONFIRMADA, COMPLETADA o CANCELADA")
    private String estado;

    private String observaciones;

    private LocalDateTime fechaCreacion;
}


