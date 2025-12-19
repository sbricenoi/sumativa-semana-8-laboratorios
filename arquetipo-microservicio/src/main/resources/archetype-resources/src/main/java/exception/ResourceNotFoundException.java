package ${package}.exception;

/**
 * Excepción lanzada cuando un recurso no es encontrado
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resource, Long id) {
        super(String.format("%s con ID %d no encontrado", resource, id));
    }
}

