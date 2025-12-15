package com.duoc.laboratorio.resultados.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un Resultado de Análisis Clínico
 * Tabla: RESULTADOS_ANALISIS
 */
@Entity
@Table(name = "RESULTADOS_ANALISIS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_resultado")
    @SequenceGenerator(name = "seq_resultado", sequenceName = "SEQ_RESULTADO", allocationSize = 1)
    @Column(name = "ID_RESULTADO")
    private Long idResultado;

    @Column(name = "ID_CITA", nullable = false, unique = true)
    private Long idCita;

    @Column(name = "ID_LABORATORISTA", nullable = false)
    private Long idLaboratorista;

    @Column(name = "ARCHIVO_PDF", length = 500)
    private String archivoPdf;

    @Column(name = "OBSERVACIONES", length = 1000)
    private String observaciones;

    @Column(name = "FECHA_RESULTADO", nullable = false)
    private LocalDateTime fechaResultado;

    @Column(name = "ESTADO", length = 50, nullable = false)
    private String estado;

    @Lob
    @Column(name = "VALORES_MEDIDOS")
    private String valoresMedidos;

    @Column(name = "FECHA_CREACION", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (fechaResultado == null) {
            fechaResultado = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "PENDIENTE";
        }
    }
}

