package com.duoc.laboratorio.laboratorios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad que representa la relación N:M entre Laboratorios y Tipos de Análisis
 * 
 * Esta entidad mapea la tabla LABORATORIO_ANALISIS en Oracle Database
 */
@Entity
@Table(name = "LABORATORIO_ANALISIS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LaboratorioAnalisis.LaboratorioAnalisisId.class)
public class LaboratorioAnalisis {

    @Id
    @Column(name = "ID_LABORATORIO")
    private Long idLaboratorio;

    @Id
    @Column(name = "ID_TIPO_ANALISIS")
    private Long idTipoAnalisis;

    @Column(name = "DISPONIBLE")
    private Integer disponible;

    @Column(name = "FECHA_ASIGNACION")
    private LocalDateTime fechaAsignacion;

    @PrePersist
    protected void onCreate() {
        if (fechaAsignacion == null) {
            fechaAsignacion = LocalDateTime.now();
        }
        if (disponible == null) {
            disponible = 1;
        }
    }

    /**
     * Clase interna para la clave compuesta
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LaboratorioAnalisisId implements Serializable {
        private Long idLaboratorio;
        private Long idTipoAnalisis;
    }
}


