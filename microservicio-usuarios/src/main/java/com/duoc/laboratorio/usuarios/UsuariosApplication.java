package com.duoc.laboratorio.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsuariosApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsuariosApplication.class, args);
        System.out.println("===========================================");
        System.out.println("Microservicio de Usuarios ejecutándose...");
        System.out.println("Puerto: 8081");
        System.out.println("URL Base: http://localhost:8081/api");
        System.out.println("===========================================");
    }
}
