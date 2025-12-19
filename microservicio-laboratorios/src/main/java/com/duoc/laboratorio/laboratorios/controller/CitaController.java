package com.duoc.laboratorio.laboratorios.controller;

import com.duoc.laboratorio.laboratorios.dto.ApiResponse;
import com.duoc.laboratorio.laboratorios.dto.CitaDTO;
import com.duoc.laboratorio.laboratorios.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de citas
 * 
 * Endpoints disponibles:
 * - POST   /api/citas - Crear cita
 * - GET    /api/citas - Listar todas las citas
 * - GET    /api/citas/{id} - Obtener cita por ID
 * - GET    /api/citas/paciente/{idPaciente} - Obtener citas por paciente
 * - GET    /api/citas/laboratorio/{idLaboratorio} - Obtener citas por laboratorio
 * - GET    /api/citas/laboratorio/{idLaboratorio}/proximas - Obtener próximas citas
 * - GET    /api/citas/estado/{estado} - Obtener citas por estado
 * - PUT    /api/citas/{id} - Actualizar cita
 * - PUT    /api/citas/{id}/estado - Cambiar estado de cita
 * - DELETE /api/citas/{id} - Eliminar cita
 */
@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    /**
     * POST /api/citas
     * Crear nueva cita
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CitaDTO>> crearCita(
            @Valid @RequestBody CitaDTO citaDTO) {
        
        CitaDTO creada = citaService.crearCita(citaDTO);
        ApiResponse<CitaDTO> response = ApiResponse.success(
            "Cita creada exitosamente",
            creada
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/citas
     * Obtener todas las citas
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerTodas() {
        List<CitaDTO> citas = citaService.obtenerTodas();
        ApiResponse<List<CitaDTO>> response = ApiResponse.success(
            "Lista de citas obtenida exitosamente",
            citas
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/citas/{id}
     * Obtener cita por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaDTO>> obtenerPorId(@PathVariable Long id) {
        CitaDTO cita = citaService.obtenerPorId(id);
        ApiResponse<CitaDTO> response = ApiResponse.success(
            "Cita encontrada",
            cita
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/citas/paciente/{idPaciente}
     * Obtener citas por paciente
     */
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerPorPaciente(
            @PathVariable Long idPaciente) {
        
        List<CitaDTO> citas = citaService.obtenerPorPaciente(idPaciente);
        ApiResponse<List<CitaDTO>> response = ApiResponse.success(
            "Citas del paciente obtenidas exitosamente",
            citas
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/citas/laboratorio/{idLaboratorio}
     * Obtener citas por laboratorio
     */
    @GetMapping("/laboratorio/{idLaboratorio}")
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerPorLaboratorio(
            @PathVariable Long idLaboratorio) {
        
        List<CitaDTO> citas = citaService.obtenerPorLaboratorio(idLaboratorio);
        ApiResponse<List<CitaDTO>> response = ApiResponse.success(
            "Citas del laboratorio obtenidas exitosamente",
            citas
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/citas/laboratorio/{idLaboratorio}/proximas
     * Obtener próximas citas de un laboratorio
     */
    @GetMapping("/laboratorio/{idLaboratorio}/proximas")
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerProximasCitas(
            @PathVariable Long idLaboratorio) {
        
        List<CitaDTO> citas = citaService.obtenerProximasCitas(idLaboratorio);
        ApiResponse<List<CitaDTO>> response = ApiResponse.success(
            "Próximas citas obtenidas exitosamente",
            citas
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/citas/estado/{estado}
     * Obtener citas por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<CitaDTO>>> obtenerPorEstado(
            @PathVariable String estado) {
        
        List<CitaDTO> citas = citaService.obtenerPorEstado(estado);
        ApiResponse<List<CitaDTO>> response = ApiResponse.success(
            "Citas con estado " + estado + " obtenidas exitosamente",
            citas
        );
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/citas/{id}
     * Actualizar cita
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaDTO>> actualizarCita(
            @PathVariable Long id,
            @Valid @RequestBody CitaDTO citaDTO) {
        
        CitaDTO actualizada = citaService.actualizarCita(id, citaDTO);
        ApiResponse<CitaDTO> response = ApiResponse.success(
            "Cita actualizada exitosamente",
            actualizada
        );
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/citas/{id}/estado
     * Cambiar estado de cita
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<CitaDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        
        CitaDTO actualizada = citaService.cambiarEstado(id, estado);
        ApiResponse<CitaDTO> response = ApiResponse.success(
            "Estado de cita actualizado exitosamente",
            actualizada
        );
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/citas/{id}
     * Eliminar cita
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        ApiResponse<Object> response = ApiResponse.success(
            "Cita eliminada exitosamente",
            null
        );
        return ResponseEntity.ok(response);
    }
}


