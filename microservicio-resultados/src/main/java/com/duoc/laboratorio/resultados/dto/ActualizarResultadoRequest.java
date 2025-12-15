package com.duoc.laboratorio.resultados.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para actualizar un resultado existente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarResultadoRequest {
    
    private String archivoPdf;
    private String observaciones;
    private LocalDateTime fechaResultado;
    private String estado;
    private String valoresMedidos;
}

