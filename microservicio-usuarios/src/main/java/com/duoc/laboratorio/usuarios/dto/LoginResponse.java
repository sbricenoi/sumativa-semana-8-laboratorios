package com.duoc.laboratorio.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private String token;
    private String mensaje;
}
