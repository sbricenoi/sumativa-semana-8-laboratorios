package ${package};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del Microservicio ${artifactId}
 * Puerto: ${puerto}
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("\n=======================================================");
        System.out.println("🚀 MICROSERVICIO ${artifactId.toUpperCase()} INICIADO");
        System.out.println("📍 Puerto: ${puerto}");
        System.out.println("🔗 URL Base: http://localhost:${puerto}/api");
        System.out.println("=======================================================\n");
    }
}

