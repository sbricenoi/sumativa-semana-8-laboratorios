package com.duoc.laboratorio.laboratorios.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para transferencia de datos de Tipo de Análisis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoAnalisisDTO {

    private Long idTipoAnalisis;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @NotNull(message = "El tiempo de entrega es obligatorio")
    @Min(value = 1, message = "El tiempo de entrega debe ser al menos 1 día")
    private Integer tiempoEntregaDias;

    private Integer activo;

    private LocalDateTime fechaCreacion;
}


