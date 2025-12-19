package com.duoc.laboratorio.laboratorios.controller;

import com.duoc.laboratorio.laboratorios.dto.ApiResponse;
import com.duoc.laboratorio.laboratorios.dto.TipoAnalisisDTO;
import com.duoc.laboratorio.laboratorios.service.TipoAnalisisService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de tipos de análisis
 * 
 * Endpoints disponibles:
 * - POST   /api/analisis - Crear tipo de análisis
 * - GET    /api/analisis - Listar todos los tipos de análisis
 * - GET    /api/analisis/{id} - Obtener tipo de análisis por ID
 * - GET    /api/analisis/activos - Obtener tipos de análisis activos
 * - GET    /api/analisis/buscar?nombre={nombre} - Buscar por nombre
 * - PUT    /api/analisis/{id} - Actualizar tipo de análisis
 * - DELETE /api/analisis/{id} - Eliminar tipo de análisis
 */
@RestController
@RequestMapping("/api/analisis")
public class TipoAnalisisController {

    @Autowired
    private TipoAnalisisService tipoAnalisisService;

    /**
     * POST /api/analisis
     * Crear nuevo tipo de análisis
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TipoAnalisisDTO>> crearTipoAnalisis(
            @Valid @RequestBody TipoAnalisisDTO tipoAnalisisDTO) {
        
        TipoAnalisisDTO creado = tipoAnalisisService.crearTipoAnalisis(tipoAnalisisDTO);
        ApiResponse<TipoAnalisisDTO> response = ApiResponse.success(
            "Tipo de análisis creado exitosamente",
            creado
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/analisis
     * Obtener todos los tipos de análisis
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TipoAnalisisDTO>>> obtenerTodos() {
        List<TipoAnalisisDTO> analisis = tipoAnalisisService.obtenerTodos();
        ApiResponse<List<TipoAnalisisDTO>> response = ApiResponse.success(
            "Lista de tipos de análisis obtenida exitosamente",
            analisis
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/analisis/{id}
     * Obtener tipo de análisis por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoAnalisisDTO>> obtenerPorId(@PathVariable Long id) {
        TipoAnalisisDTO analisis = tipoAnalisisService.obtenerPorId(id);
        ApiResponse<TipoAnalisisDTO> response = ApiResponse.success(
            "Tipo de análisis encontrado",
            analisis
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/analisis/activos
     * Obtener tipos de análisis activos
     */
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<TipoAnalisisDTO>>> obtenerActivos() {
        List<TipoAnalisisDTO> analisis = tipoAnalisisService.obtenerActivos();
        ApiResponse<List<TipoAnalisisDTO>> response = ApiResponse.success(
            "Tipos de análisis activos obtenidos exitosamente",
            analisis
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/analisis/buscar?nombre={nombre}
     * Buscar tipos de análisis por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<TipoAnalisisDTO>>> buscar(
            @RequestParam String nombre) {
        
        List<TipoAnalisisDTO> analisis = tipoAnalisisService.buscarPorNombre(nombre);
        ApiResponse<List<TipoAnalisisDTO>> response = ApiResponse.success(
            "Búsqueda completada",
            analisis
        );
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/analisis/{id}
     * Actualizar tipo de análisis
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoAnalisisDTO>> actualizarTipoAnalisis(
            @PathVariable Long id,
            @Valid @RequestBody TipoAnalisisDTO tipoAnalisisDTO) {
        
        TipoAnalisisDTO actualizado = tipoAnalisisService.actualizarTipoAnalisis(id, tipoAnalisisDTO);
        ApiResponse<TipoAnalisisDTO> response = ApiResponse.success(
            "Tipo de análisis actualizado exitosamente",
            actualizado
        );
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/analisis/{id}
     * Eliminar tipo de análisis (borrado lógico)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminarTipoAnalisis(@PathVariable Long id) {
        tipoAnalisisService.eliminarTipoAnalisis(id);
        ApiResponse<Object> response = ApiResponse.success(
            "Tipo de análisis eliminado exitosamente",
            null
        );
        return ResponseEntity.ok(response);
    }
}


