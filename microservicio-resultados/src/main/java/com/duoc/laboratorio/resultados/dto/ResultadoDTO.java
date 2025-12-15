package com.duoc.laboratorio.resultados.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferencia de datos de Resultados
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoDTO {
    
    private Long idResultado;
    private Long idCita;
    private Long idLaboratorista;
    private String nombreLaboratorista;
    private Long idPaciente;
    private String nombrePaciente;
    private String nombreAnalisis;
    private String archivoPdf;
    private String observaciones;
    private LocalDateTime fechaResultado;
    private String estado;
    private String valoresMedidos;
    private LocalDateTime fechaCreacion;
}

