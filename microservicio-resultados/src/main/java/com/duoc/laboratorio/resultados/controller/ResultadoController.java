package com.duoc.laboratorio.resultados.controller;

import com.duoc.laboratorio.resultados.dto.ActualizarResultadoRequest;
import com.duoc.laboratorio.resultados.dto.ApiResponse;
import com.duoc.laboratorio.resultados.dto.CrearResultadoRequest;
import com.duoc.laboratorio.resultados.dto.ResultadoDTO;
import com.duoc.laboratorio.resultados.service.ResultadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de Resultados de Análisis
 * Base URL: /api/resultados
 */
@RestController
@RequestMapping("/api/resultados")
@RequiredArgsConstructor
public class ResultadoController {

    private final ResultadoService resultadoService;

    /**
     * Crear un nuevo resultado
     * POST /api/resultados
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ResultadoDTO>> crearResultado(
            @Valid @RequestBody CrearResultadoRequest request) {
        ResultadoDTO resultado = resultadoService.crearResultado(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resultado creado exitosamente", resultado));
    }

    /**
     * Obtener todos los resultados
     * GET /api/resultados
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ResultadoDTO>>> listarResultados() {
        List<ResultadoDTO> resultados = resultadoService.listarTodos();
        return ResponseEntity.ok(
            ApiResponse.success("Resultados obtenidos exitosamente", resultados)
        );
    }

    /**
     * Obtener resultado por ID
     * GET /api/resultados/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResultadoDTO>> obtenerResultadoPorId(
            @PathVariable Long id) {
        ResultadoDTO resultado = resultadoService.obtenerPorId(id);
        return ResponseEntity.ok(
            ApiResponse.success("Resultado encontrado", resultado)
        );
    }

    /**
     * Obtener resultado por ID de cita
     * GET /api/resultados/cita/{idCita}
     */
    @GetMapping("/cita/{idCita}")
    public ResponseEntity<ApiResponse<ResultadoDTO>> obtenerResultadoPorCita(
            @PathVariable Long idCita) {
        ResultadoDTO resultado = resultadoService.obtenerPorIdCita(idCita);
        return ResponseEntity.ok(
            ApiResponse.success("Resultado de la cita encontrado", resultado)
        );
    }

    /**
     * Obtener resultados por laboratorista
     * GET /api/resultados/laboratorista/{idLaboratorista}
     */
    @GetMapping("/laboratorista/{idLaboratorista}")
    public ResponseEntity<ApiResponse<List<ResultadoDTO>>> obtenerResultadosPorLaboratorista(
            @PathVariable Long idLaboratorista) {
        List<ResultadoDTO> resultados = resultadoService.listarPorLaboratorista(idLaboratorista);
        return ResponseEntity.ok(
            ApiResponse.success("Resultados del laboratorista obtenidos", resultados)
        );
    }

    /**
     * Obtener resultados por estado
     * GET /api/resultados/estado/{estado}
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<ResultadoDTO>>> obtenerResultadosPorEstado(
            @PathVariable String estado) {
        List<ResultadoDTO> resultados = resultadoService.listarPorEstado(estado);
        return ResponseEntity.ok(
            ApiResponse.success("Resultados con estado " + estado + " obtenidos", resultados)
        );
    }

    /**
     * Actualizar un resultado
     * PUT /api/resultados/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResultadoDTO>> actualizarResultado(
            @PathVariable Long id,
            @RequestBody ActualizarResultadoRequest request) {
        ResultadoDTO resultado = resultadoService.actualizarResultado(id, request);
        return ResponseEntity.ok(
            ApiResponse.success("Resultado actualizado exitosamente", resultado)
        );
    }

    /**
     * Cambiar estado de un resultado
     * PATCH /api/resultados/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<ResultadoDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        ResultadoDTO resultado = resultadoService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(
            ApiResponse.success("Estado del resultado actualizado", resultado)
        );
    }

    /**
     * Eliminar un resultado
     * DELETE /api/resultados/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarResultado(@PathVariable Long id) {
        resultadoService.eliminarResultado(id);
        return ResponseEntity.ok(
            ApiResponse.success("Resultado eliminado exitosamente", null)
        );
    }

    /**
     * Health check del microservicio
     * GET /api/resultados/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> healthCheck() {
        Map<String, String> health = Map.of(
            "status", "UP",
            "servicio", "Microservicio de Resultados",
            "version", "1.0.0",
            "puerto", "8083"
        );
        return ResponseEntity.ok(
            ApiResponse.success("Microservicio operativo", health)
        );
    }
}

