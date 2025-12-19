package ${package}.exception;

/**
 * Excepción lanzada cuando hay un error en la petición del cliente
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
}

