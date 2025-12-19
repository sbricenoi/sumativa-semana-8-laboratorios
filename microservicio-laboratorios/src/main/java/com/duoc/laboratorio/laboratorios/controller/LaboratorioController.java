package com.duoc.laboratorio.laboratorios.controller;

import com.duoc.laboratorio.laboratorios.dto.ApiResponse;
import com.duoc.laboratorio.laboratorios.dto.AsignarAnalisisRequest;
import com.duoc.laboratorio.laboratorios.dto.LaboratorioDTO;
import com.duoc.laboratorio.laboratorios.model.LaboratorioAnalisis;
import com.duoc.laboratorio.laboratorios.service.LaboratorioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de laboratorios
 * 
 * Endpoints disponibles:
 * - POST   /api/laboratorios - Crear laboratorio
 * - GET    /api/laboratorios - Listar todos los laboratorios
 * - GET    /api/laboratorios/{id} - Obtener laboratorio por ID
 * - GET    /api/laboratorios/activos - Obtener laboratorios activos
 * - GET    /api/laboratorios/especialidad/{especialidad} - Filtrar por especialidad
 * - GET    /api/laboratorios/buscar?nombre={nombre} - Buscar por nombre
 * - PUT    /api/laboratorios/{id} - Actualizar laboratorio
 * - DELETE /api/laboratorios/{id} - Eliminar laboratorio
 * - POST   /api/laboratorios/{id}/analisis - Asignar análisis a laboratorio
 * - GET    /api/laboratorios/{id}/analisis - Obtener análisis asignados
 */
@RestController
@RequestMapping("/api/laboratorios")
public class LaboratorioController {

    @Autowired
    private LaboratorioService laboratorioService;

    /**
     * POST /api/laboratorios
     * Crear nuevo laboratorio
     */
    @PostMapping
    public ResponseEntity<ApiResponse<LaboratorioDTO>> crearLaboratorio(
            @Valid @RequestBody LaboratorioDTO laboratorioDTO) {
        
        LaboratorioDTO creado = laboratorioService.crearLaboratorio(laboratorioDTO);
        ApiResponse<LaboratorioDTO> response = ApiResponse.success(
            "Laboratorio creado exitosamente",
            creado
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/laboratorios
     * Obtener todos los laboratorios
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<LaboratorioDTO>>> obtenerTodos() {
        List<LaboratorioDTO> laboratorios = laboratorioService.obtenerTodos();
        ApiResponse<List<LaboratorioDTO>> response = ApiResponse.success(
            "Lista de laboratorios obtenida exitosamente",
            laboratorios
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/laboratorios/{id}
     * Obtener laboratorio por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LaboratorioDTO>> obtenerPorId(@PathVariable Long id) {
        LaboratorioDTO laboratorio = laboratorioService.obtenerPorId(id);
        ApiResponse<LaboratorioDTO> response = ApiResponse.success(
            "Laboratorio encontrado",
            laboratorio
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/laboratorios/activos
     * Obtener laboratorios activos
     */
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<LaboratorioDTO>>> obtenerActivos() {
        List<LaboratorioDTO> laboratorios = laboratorioService.obtenerActivos();
        ApiResponse<List<LaboratorioDTO>> response = ApiResponse.success(
            "Laboratorios activos obtenidos exitosamente",
            laboratorios
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/laboratorios/especialidad/{especialidad}
     * Obtener laboratorios por especialidad
     */
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<ApiResponse<List<LaboratorioDTO>>> obtenerPorEspecialidad(
            @PathVariable String especialidad) {
        
        List<LaboratorioDTO> laboratorios = laboratorioService.obtenerPorEspecialidad(especialidad);
        ApiResponse<List<LaboratorioDTO>> response = ApiResponse.success(
            "Laboratorios con especialidad " + especialidad + " obtenidos exitosamente",
            laboratorios
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/laboratorios/buscar?nombre={nombre}
     * Buscar laboratorios por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<LaboratorioDTO>>> buscar(
            @RequestParam String nombre) {
        
        List<LaboratorioDTO> laboratorios = laboratorioService.buscarPorNombre(nombre);
        ApiResponse<List<LaboratorioDTO>> response = ApiResponse.success(
            "Búsqueda completada",
            laboratorios
        );
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/laboratorios/{id}
     * Actualizar laboratorio
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LaboratorioDTO>> actualizarLaboratorio(
            @PathVariable Long id,
            @Valid @RequestBody LaboratorioDTO laboratorioDTO) {
        
        LaboratorioDTO actualizado = laboratorioService.actualizarLaboratorio(id, laboratorioDTO);
        ApiResponse<LaboratorioDTO> response = ApiResponse.success(
            "Laboratorio actualizado exitosamente",
            actualizado
        );
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/laboratorios/{id}
     * Eliminar laboratorio (borrado lógico)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminarLaboratorio(@PathVariable Long id) {
        laboratorioService.eliminarLaboratorio(id);
        ApiResponse<Object> response = ApiResponse.success(
            "Laboratorio eliminado exitosamente",
            null
        );
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/laboratorios/{id}/analisis
     * Asignar análisis a laboratorio
     */
    @PostMapping("/{id}/analisis")
    public ResponseEntity<ApiResponse<Object>> asignarAnalisis(
            @PathVariable Long id,
            @Valid @RequestBody AsignarAnalisisRequest request) {
        
        laboratorioService.asignarAnalisis(id, request);
        ApiResponse<Object> response = ApiResponse.success(
            "Análisis asignado al laboratorio exitosamente",
            null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/laboratorios/{id}/analisis
     * Obtener análisis asignados a un laboratorio
     */
    @GetMapping("/{id}/analisis")
    public ResponseEntity<ApiResponse<List<LaboratorioAnalisis>>> obtenerAnalisisAsignados(
            @PathVariable Long id) {
        
        List<LaboratorioAnalisis> analisis = laboratorioService.obtenerAnalisisAsignados(id);
        ApiResponse<List<LaboratorioAnalisis>> response = ApiResponse.success(
            "Análisis asignados obtenidos exitosamente",
            analisis
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/laboratorios/health
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        ApiResponse<String> response = ApiResponse.success(
            "Microservicio de Laboratorios funcionando correctamente",
            "OK"
        );
        return ResponseEntity.ok(response);
    }
}


