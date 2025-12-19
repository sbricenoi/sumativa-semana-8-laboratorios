package com.duoc.laboratorio.laboratorios.controller;

import com.duoc.laboratorio.laboratorios.dto.AsignarAnalisisRequest;
import com.duoc.laboratorio.laboratorios.dto.LaboratorioDTO;
import com.duoc.laboratorio.laboratorios.exception.BadRequestException;
import com.duoc.laboratorio.laboratorios.exception.ResourceNotFoundException;
import com.duoc.laboratorio.laboratorios.model.LaboratorioAnalisis;
import com.duoc.laboratorio.laboratorios.service.LaboratorioService;
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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitarios para LaboratorioController
 */
@WebMvcTest(LaboratorioController.class)
@DisplayName("Tests de LaboratorioController")
class LaboratorioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LaboratorioService laboratorioService;

    private LaboratorioDTO laboratorioDTO;

    @BeforeEach
    void setUp() {
        laboratorioDTO = new LaboratorioDTO();
        laboratorioDTO.setIdLaboratorio(1L);
        laboratorioDTO.setNombre("Laboratorio Central");
        laboratorioDTO.setDireccion("Av. Principal 123");
        laboratorioDTO.setTelefono("+56912345678");
        laboratorioDTO.setEmail("lab@central.cl");
        laboratorioDTO.setEspecialidad("Análisis General");
        laboratorioDTO.setActivo(1);
        laboratorioDTO.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    @DisplayName("POST /api/laboratorios - Debe crear laboratorio exitosamente")
    void testCrearLaboratorio_Exitoso() throws Exception {
        // Given
        when(laboratorioService.crearLaboratorio(any(LaboratorioDTO.class)))
            .thenReturn(laboratorioDTO);

        // When & Then
        mockMvc.perform(post("/api/laboratorios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(laboratorioDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Laboratorio creado exitosamente"))
            .andExpect(jsonPath("$.data.nombre").value("Laboratorio Central"));

        verify(laboratorioService, times(1)).crearLaboratorio(any(LaboratorioDTO.class));
    }

    @Test
    @DisplayName("POST /api/laboratorios - Debe retornar error si email duplicado")
    void testCrearLaboratorio_EmailDuplicado() throws Exception {
        // Given
        when(laboratorioService.crearLaboratorio(any(LaboratorioDTO.class)))
            .thenThrow(new BadRequestException("El email ya está registrado"));

        // When & Then
        mockMvc.perform(post("/api/laboratorios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(laboratorioDTO)))
            .andExpect(status().isBadRequest());

        verify(laboratorioService, times(1)).crearLaboratorio(any(LaboratorioDTO.class));
    }

    @Test
    @DisplayName("GET /api/laboratorios - Debe obtener todos los laboratorios")
    void testObtenerTodos() throws Exception {
        // Given
        List<LaboratorioDTO> laboratorios = Arrays.asList(laboratorioDTO);
        when(laboratorioService.obtenerTodos()).thenReturn(laboratorios);

        // When & Then
        mockMvc.perform(get("/api/laboratorios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data", hasSize(1)))
            .andExpect(jsonPath("$.data[0].nombre").value("Laboratorio Central"));

        verify(laboratorioService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("GET /api/laboratorios/{id} - Debe obtener laboratorio por ID")
    void testObtenerPorId_Exitoso() throws Exception {
        // Given
        when(laboratorioService.obtenerPorId(1L)).thenReturn(laboratorioDTO);

        // When & Then
        mockMvc.perform(get("/api/laboratorios/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data.idLaboratorio").value(1))
            .andExpect(jsonPath("$.data.nombre").value("Laboratorio Central"));

        verify(laboratorioService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /api/laboratorios/{id} - Debe retornar 404 si no encuentra laboratorio")
    void testObtenerPorId_NoEncontrado() throws Exception {
        // Given
        when(laboratorioService.obtenerPorId(999L))
            .thenThrow(new ResourceNotFoundException("Laboratorio no encontrado con ID: 999"));

        // When & Then
        mockMvc.perform(get("/api/laboratorios/999"))
            .andExpect(status().isNotFound());

        verify(laboratorioService, times(1)).obtenerPorId(999L);
    }

    @Test
    @DisplayName("GET /api/laboratorios/activos - Debe obtener laboratorios activos")
    void testObtenerActivos() throws Exception {
        // Given
        List<LaboratorioDTO> laboratorios = Arrays.asList(laboratorioDTO);
        when(laboratorioService.obtenerActivos()).thenReturn(laboratorios);

        // When & Then
        mockMvc.perform(get("/api/laboratorios/activos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data", hasSize(1)))
            .andExpect(jsonPath("$.data[0].activo").value(1));

        verify(laboratorioService, times(1)).obtenerActivos();
    }

    @Test
    @DisplayName("GET /api/laboratorios/especialidad/{especialidad} - Debe filtrar por especialidad")
    void testObtenerPorEspecialidad() throws Exception {
        // Given
        List<LaboratorioDTO> laboratorios = Arrays.asList(laboratorioDTO);
        when(laboratorioService.obtenerPorEspecialidad("Análisis General"))
            .thenReturn(laboratorios);

        // When & Then
        mockMvc.perform(get("/api/laboratorios/especialidad/Análisis General"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data", hasSize(1)))
            .andExpect(jsonPath("$.data[0].especialidad").value("Análisis General"));

        verify(laboratorioService, times(1)).obtenerPorEspecialidad("Análisis General");
    }

    @Test
    @DisplayName("GET /api/laboratorios/buscar?nombre={nombre} - Debe buscar por nombre")
    void testBuscar() throws Exception {
        // Given
        List<LaboratorioDTO> laboratorios = Arrays.asList(laboratorioDTO);
        when(laboratorioService.buscarPorNombre("Central")).thenReturn(laboratorios);

        // When & Then
        mockMvc.perform(get("/api/laboratorios/buscar")
                .param("nombre", "Central"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data", hasSize(1)));

        verify(laboratorioService, times(1)).buscarPorNombre("Central");
    }

    @Test
    @DisplayName("PUT /api/laboratorios/{id} - Debe actualizar laboratorio")
    void testActualizarLaboratorio_Exitoso() throws Exception {
        // Given
        laboratorioDTO.setNombre("Laboratorio Central Actualizado");
        when(laboratorioService.actualizarLaboratorio(eq(1L), any(LaboratorioDTO.class)))
            .thenReturn(laboratorioDTO);

        // When & Then
        mockMvc.perform(put("/api/laboratorios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(laboratorioDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Laboratorio actualizado exitosamente"));

        verify(laboratorioService, times(1)).actualizarLaboratorio(eq(1L), any(LaboratorioDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/laboratorios/{id} - Debe eliminar laboratorio")
    void testEliminarLaboratorio() throws Exception {
        // Given
        doNothing().when(laboratorioService).eliminarLaboratorio(1L);

        // When & Then
        mockMvc.perform(delete("/api/laboratorios/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Laboratorio eliminado exitosamente"));

        verify(laboratorioService, times(1)).eliminarLaboratorio(1L);
    }

    @Test
    @DisplayName("POST /api/laboratorios/{id}/analisis - Debe asignar análisis a laboratorio")
    void testAsignarAnalisis() throws Exception {
        // Given
        AsignarAnalisisRequest request = new AsignarAnalisisRequest();
        request.setIdTipoAnalisis(1L);
        request.setDisponible(1);

        doNothing().when(laboratorioService).asignarAnalisis(eq(1L), any(AsignarAnalisisRequest.class));

        // When & Then
        mockMvc.perform(post("/api/laboratorios/1/analisis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Análisis asignado al laboratorio exitosamente"));

        verify(laboratorioService, times(1)).asignarAnalisis(eq(1L), any(AsignarAnalisisRequest.class));
    }

    @Test
    @DisplayName("GET /api/laboratorios/{id}/analisis - Debe obtener análisis asignados")
    void testObtenerAnalisisAsignados() throws Exception {
        // Given
        LaboratorioAnalisis asignacion = new LaboratorioAnalisis();
        asignacion.setIdLaboratorio(1L);
        asignacion.setIdTipoAnalisis(1L);
        asignacion.setDisponible(1);

        List<LaboratorioAnalisis> analisis = Arrays.asList(asignacion);
        when(laboratorioService.obtenerAnalisisAsignados(1L)).thenReturn(analisis);

        // When & Then
        mockMvc.perform(get("/api/laboratorios/1/analisis"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data", hasSize(1)));

        verify(laboratorioService, times(1)).obtenerAnalisisAsignados(1L);
    }

    @Test
    @DisplayName("GET /api/laboratorios/health - Debe retornar OK")
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/laboratorios/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value("Microservicio de Laboratorios funcionando correctamente"))
            .andExpect(jsonPath("$.data").value("OK"));
    }
}


