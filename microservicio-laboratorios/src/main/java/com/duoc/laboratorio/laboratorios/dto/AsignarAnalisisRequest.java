package com.duoc.laboratorio.laboratorios.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la petición de asignación de análisis a laboratorio
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignarAnalisisRequest {

    @NotNull(message = "El ID del tipo de análisis es obligatorio")
    private Long idTipoAnalisis;

    private Integer disponible; // 1 = disponible, 0 = no disponible
}


