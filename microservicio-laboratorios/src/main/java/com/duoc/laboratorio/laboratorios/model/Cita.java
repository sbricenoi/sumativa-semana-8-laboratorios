package com.duoc.laboratorio.laboratorios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa una Cita para análisis clínico
 * 
 * Esta entidad mapea la tabla CITAS en Oracle Database
 */
@Entity
@Table(name = "CITAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cita")
    @SequenceGenerator(name = "seq_cita", sequenceName = "SEQ_CITA", allocationSize = 1)
    @Column(name = "ID_CITA")
    private Long idCita;

    @Column(name = "ID_PACIENTE", nullable = false)
    private Long idPaciente;

    @Column(name = "ID_LABORATORIO", nullable = false)
    private Long idLaboratorio;

    @Column(name = "ID_TIPO_ANALISIS", nullable = false)
    private Long idTipoAnalisis;

    @Column(name = "FECHA_CITA", nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "ESTADO", length = 50)
    private String estado;

    @Column(name = "OBSERVACIONES", length = 500)
    private String observaciones;

    @Column(name = "FECHA_CREACION")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "PROGRAMADA";
        }
    }
}


