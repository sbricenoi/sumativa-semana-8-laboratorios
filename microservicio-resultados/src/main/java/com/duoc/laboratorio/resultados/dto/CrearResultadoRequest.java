package com.duoc.laboratorio.resultados.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para crear un nuevo resultado de análisis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearResultadoRequest {
    
    @NotNull(message = "El ID de la cita es obligatorio")
    private Long idCita;
    
    @NotNull(message = "El ID del laboratorista es obligatorio")
    private Long idLaboratorista;
    
    private String archivoPdf;
    private String observaciones;
    private LocalDateTime fechaResultado;
    private String estado;
    private String valoresMedidos;
}

