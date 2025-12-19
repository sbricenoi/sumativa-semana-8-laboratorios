package com.duoc.laboratorio.resultados.controller;

import com.duoc.laboratorio.resultados.dto.ActualizarResultadoRequest;
import com.duoc.laboratorio.resultados.dto.CrearResultadoRequest;
import com.duoc.laboratorio.resultados.dto.ResultadoDTO;
import com.duoc.laboratorio.resultados.exception.BadRequestException;
import com.duoc.laboratorio.resultados.exception.ResourceNotFoundException;
import com.duoc.laboratorio.resultados.service.ResultadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitarios para ResultadoController
 */
@WebMvcTest(ResultadoController.class)
@DisplayName("Tests de ResultadoController")
class ResultadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResultadoService resultadoService;

    private ResultadoDTO resultadoDTO;
    private CrearResultadoRequest crearRequest;

    @BeforeEach
    void setUp() {
        // Configurar DTO de resultado
        resultadoDTO = new ResultadoDTO();
        resultadoDTO.setIdResultado(1L);
        resultadoDTO.setIdCita(1L);
        resultadoDTO.setIdLaboratorista(3L);
        resultadoDTO.setArchivoPdf("/resultados/2025/resultado_1.pdf");
        resultadoDTO.setObservaciones("Resultado normal");
        resultadoDTO.setFechaResultado(LocalDateTime.now());
        resultadoDTO.setEstado("COMPLETADO");
        resultadoDTO.setValoresMedidos("{\"hemoglobina\": \"14.5 g/dL\"}");
        resultadoDTO.setFechaCreacion(LocalDateTime.now());

        // Configurar request de creación
        crearRequest = new CrearResultadoRequest();
        crearRequest.setIdCita(1L);
        crearRequest.setIdLaboratorista(3L);
        crearRequest.setObservaciones("Resultado normal");
        crearRequest.setEstado("PENDIENTE");
    }

    @Test
    @DisplayName("POST /api/resultados - Debe crear resultado exitosamente")
    void testCrearResultado_Exitoso() throws Exception {
        // Given
        when(resultadoService.crearResultado(any(CrearResultadoRequest.class)))
            .thenReturn(resultadoDTO);

        // When & Then
        mockMvc.perform(post("/api/resultados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(crearRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Resultado creado exitosamente"))
            .andExpect(jsonPath("$.data.idCita").value(1));

        verify(resultadoService, times(1)).crearResultado(any(CrearResultadoRequest.class));
    }

    @Test
    @DisplayName("POST /api/resultados - Debe retornar error si cita ya tiene resultado")
    void testCrearResultado_CitaDuplicada() throws Exception {
        // Given
        when(resultadoService.crearResultado(any(CrearResultadoRequest.class)))
            .thenThrow(new BadRequestException("Ya existe un resultado para la cita"));

        // When & Then
        mockMvc.perform(post("/api/resultados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(crearRequest)))
            .andExpect(status().isBadRequest());

        verify(resultadoService, times(1)).crearResultado(any(CrearResultadoRequest.class));
    }

    @Test
    @DisplayName("GET /api/resultados - Debe listar todos los resultados")
    void testListarResultados() throws Exception {
        // Given
        List<ResultadoDTO> resultados = Arrays.asList(resultadoDTO);
        when(resultadoService.listarTodos()).thenReturn(resultados);

        // When & Then
        mockMvc.perform(get("/api/resultados"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data", hasSize(1)))
            .andExpect(jsonPath("$.data[0].estado").value("COMPLETADO"));

        verify(resultadoService, times(1)).listarTodos();
    }

    @Test
    @DisplayName("GET /api/resultados/{id} - Debe obtener resultado por ID")
    void testObtenerResultadoPorId_Exitoso() throws Exception {
        // Given
        when(resultadoService.obtenerPorId(1L)).thenReturn(resultadoDTO);

        // When & Then
        mockMvc.perform(get("/api/resultados/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data.idResultado").value(1))
            .andExpect(jsonPath("$.data.estado").value("COMPLETADO"));

        verify(resultadoService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /api/resultados/{id} - Debe retornar 404 si no encuentra resultado")
    void testObtenerResultadoPorId_NoEncontrado() throws Exception {
        // Given
        when(resultadoService.obtenerPorId(999L))
            .thenThrow(new ResourceNotFoundException("Resultado", 999L));

        // When & Then
        mockMvc.perform(get("/api/resultados/999"))
            .andExpect(status().isNotFound());

        verify(resultadoService, times(1)).obtenerPorId(999L);
    }

    @Test
    @DisplayName("GET /api/resultados/cita/{idCita} - Debe obtener resultado por ID de cita")
    void testObtenerResultadoPorCita() throws Exception {
        // Given
        when(resultadoService.obtenerPorIdCita(1L)).thenReturn(resultadoDTO);

        // When & Then
        mockMvc.perform(get("/api/resultados/cita/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data.idCita").value(1));

        verify(resultadoService, times(1)).obtenerPorIdCita(1L);
    }

    @Test
    @DisplayName("GET /api/resultados/laboratorista/{id} - Debe obtener resultados por laboratorista")
    void testObtenerResultadosPorLaboratorista() throws Exception {
        // Given
        List<ResultadoDTO> resultados = Arrays.asList(resultadoDTO);
        when(resultadoService.listarPorLaboratorista(3L)).thenReturn(resultados);

        // When & Then
        mockMvc.perform(get("/api/resultados/laboratorista/3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data", hasSize(1)))
            .andExpect(jsonPath("$.data[0].idLaboratorista").value(3));

        verify(resultadoService, times(1)).listarPorLaboratorista(3L);
    }

    @Test
    @DisplayName("GET /api/resultados/estado/{estado} - Debe obtener resultados por estado")
    void testObtenerResultadosPorEstado() throws Exception {
        // Given
        List<ResultadoDTO> resultados = Arrays.asList(resultadoDTO);
        when(resultadoService.listarPorEstado("COMPLETADO")).thenReturn(resultados);

        // When & Then
        mockMvc.perform(get("/api/resultados/estado/COMPLETADO"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data", hasSize(1)))
            .andExpect(jsonPath("$.data[0].estado").value("COMPLETADO"));

        verify(resultadoService, times(1)).listarPorEstado("COMPLETADO");
    }

    @Test
    @DisplayName("PUT /api/resultados/{id} - Debe actualizar resultado")
    void testActualizarResultado_Exitoso() throws Exception {
        // Given
        ActualizarResultadoRequest request = new ActualizarResultadoRequest();
        request.setEstado("REVISADO");
        request.setObservaciones("Resultado revisado por especialista");

        when(resultadoService.actualizarResultado(eq(1L), any(ActualizarResultadoRequest.class)))
            .thenReturn(resultadoDTO);

        // When & Then
        mockMvc.perform(put("/api/resultados/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Resultado actualizado exitosamente"));

        verify(resultadoService, times(1)).actualizarResultado(eq(1L), any(ActualizarResultadoRequest.class));
    }

    @Test
    @DisplayName("PATCH /api/resultados/{id}/estado - Debe cambiar estado de resultado")
    void testCambiarEstado() throws Exception {
        // Given
        Map<String, String> body = new HashMap<>();
        body.put("estado", "REVISADO");

        when(resultadoService.cambiarEstado(1L, "REVISADO")).thenReturn(resultadoDTO);

        // When & Then
        mockMvc.perform(patch("/api/resultados/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Estado del resultado actualizado"));

        verify(resultadoService, times(1)).cambiarEstado(1L, "REVISADO");
    }

    @Test
    @DisplayName("PATCH /api/resultados/{id}/estado - Debe retornar error con estado inválido")
    void testCambiarEstado_EstadoInvalido() throws Exception {
        // Given
        Map<String, String> body = new HashMap<>();
        body.put("estado", "INVALIDO");

        when(resultadoService.cambiarEstado(1L, "INVALIDO"))
            .thenThrow(new BadRequestException("Estado no válido"));

        // When & Then
        mockMvc.perform(patch("/api/resultados/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest());

        verify(resultadoService, times(1)).cambiarEstado(1L, "INVALIDO");
    }

    @Test
    @DisplayName("DELETE /api/resultados/{id} - Debe eliminar resultado")
    void testEliminarResultado() throws Exception {
        // Given
        doNothing().when(resultadoService).eliminarResultado(1L);

        // When & Then
        mockMvc.perform(delete("/api/resultados/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Resultado eliminado exitosamente"));

        verify(resultadoService, times(1)).eliminarResultado(1L);
    }

    @Test
    @DisplayName("DELETE /api/resultados/{id} - Debe retornar 404 si resultado no existe")
    void testEliminarResultado_NoEncontrado() throws Exception {
        // Given
        doThrow(new ResourceNotFoundException("Resultado", 999L))
            .when(resultadoService).eliminarResultado(999L);

        // When & Then
        mockMvc.perform(delete("/api/resultados/999"))
            .andExpect(status().isNotFound());

        verify(resultadoService, times(1)).eliminarResultado(999L);
    }

    @Test
    @DisplayName("GET /api/resultados/health - Debe retornar estado del microservicio")
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/resultados/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Microservicio operativo"))
            .andExpect(jsonPath("$.data.status").value("UP"))
            .andExpect(jsonPath("$.data.servicio").value("Microservicio de Resultados"));
    }
}


