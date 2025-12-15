package com.duoc.laboratorio.resultados;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del Microservicio de Gestión de Resultados
 * Puerto: 8083
 */
@SpringBootApplication
public class ResultadosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResultadosApplication.class, args);
        System.out.println("\n=======================================================");
        System.out.println("🧪 MICROSERVICIO DE RESULTADOS INICIADO");
        System.out.println("📍 Puerto: 8083");
        System.out.println("🔗 URL Base: http://localhost:8083/api/resultados");
        System.out.println("=======================================================\n");
    }
}

