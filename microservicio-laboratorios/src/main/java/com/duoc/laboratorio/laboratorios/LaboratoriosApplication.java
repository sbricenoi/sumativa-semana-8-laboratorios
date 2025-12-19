package com.duoc.laboratorio.laboratorios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del Microservicio de Gestión de Laboratorios
 * 
 * Este microservicio se encarga de:
 * - Gestión de laboratorios clínicos (CRUD)
 * - Administración de tipos de análisis
 * - Programación y gestión de citas
 * - Asignación de análisis a laboratorios
 * 
 * @author Sistema de Gestión de Laboratorios
 * @version 1.0.0
 */
@SpringBootApplication
public class LaboratoriosApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaboratoriosApplication.class, args);
        System.out.println("================================================");
        System.out.println("Microservicio de Laboratorios ejecutándose...");
        System.out.println("Puerto: 8082");
        System.out.println("URL Base: http://localhost:8082/api");
        System.out.println("================================================");
    }
}

