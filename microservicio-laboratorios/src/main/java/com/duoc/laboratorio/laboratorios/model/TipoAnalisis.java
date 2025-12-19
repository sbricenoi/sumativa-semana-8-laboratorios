package com.duoc.laboratorio.laboratorios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa un Tipo de Análisis Clínico
 * 
 * Esta entidad mapea la tabla TIPOS_ANALISIS en Oracle Database
 */
@Entity
@Table(name = "TIPOS_ANALISIS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoAnalisis {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tipo_analisis")
    @SequenceGenerator(name = "seq_tipo_analisis", sequenceName = "SEQ_TIPO_ANALISIS", allocationSize = 1)
    @Column(name = "ID_TIPO_ANALISIS")
    private Long idTipoAnalisis;

    @Column(name = "NOMBRE", nullable = false, length = 200)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 500)
    private String descripcion;

    @Column(name = "PRECIO", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "TIEMPO_ENTREGA_DIAS", nullable = false)
    private Integer tiempoEntregaDias;

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


