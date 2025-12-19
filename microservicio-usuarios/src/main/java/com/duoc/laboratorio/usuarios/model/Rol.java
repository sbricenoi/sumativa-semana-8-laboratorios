package com.duoc.laboratorio.usuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROLES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rol")
    @SequenceGenerator(name = "seq_rol", sequenceName = "SEQ_ROL", allocationSize = 1)
    @Column(name = "ID_ROL")
    private Long idRol;

    @Column(name = "NOMBRE", nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;

    @Column(name = "PERMISOS", length = 500)
    private String permisos;
}
