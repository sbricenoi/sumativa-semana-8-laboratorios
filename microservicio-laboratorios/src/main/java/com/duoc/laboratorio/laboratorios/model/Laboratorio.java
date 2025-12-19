package com.duoc.laboratorio.laboratorios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un Laboratorio Clínico
 * 
 * Esta entidad mapea la tabla LABORATORIOS en Oracle Database
 */
@Entity
@Table(name = "LABORATORIOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Laboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_laboratorio")
    @SequenceGenerator(name = "seq_laboratorio", sequenceName = "SEQ_LABORATORIO", allocationSize = 1)
    @Column(name = "ID_LABORATORIO")
    private Long idLaboratorio;

    @Column(name = "NOMBRE", nullable = false, length = 200)
    private String nombre;

    @Column(name = "DIRECCION", nullable = false, length = 300)
    private String direccion;

    @Column(name = "TELEFONO", nullable = false, length = 20)
    private String telefono;

    @Column(name = "EMAIL", nullable = false, length = 150)
    private String email;

    @Column(name = "ESPECIALIDAD", length = 100)
    private String especialidad;

    @Column(name = "ACTIVO")
    private Integer activo;

    @Column(name = "FECHA_CREACION")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (activo == null) {
            activo = 1;
        }
    }
}


