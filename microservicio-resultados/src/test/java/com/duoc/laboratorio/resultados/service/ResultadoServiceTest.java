package com.duoc.laboratorio.resultados.service;

import com.duoc.laboratorio.resultados.dto.ActualizarResultadoRequest;
import com.duoc.laboratorio.resultados.dto.CrearResultadoRequest;
import com.duoc.laboratorio.resultados.dto.ResultadoDTO;
import com.duoc.laboratorio.resultados.exception.BadRequestException;
import com.duoc.laboratorio.resultados.exception.ResourceNotFoundException;
import com.duoc.laboratorio.resultados.model.Resultado;
import com.duoc.laboratorio.resultados.repository.ResultadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ResultadoService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de ResultadoService")
class ResultadoServiceTest {

    @Mock
    private ResultadoRepository resultadoRepository;

    @InjectMocks
    private ResultadoService resultadoService;

    private Resultado resultado;
    private CrearResultadoRequest crearRequest;
    private ActualizarResultadoRequest actualizarRequest;

    @BeforeEach
    void setUp() {
        // Configurar resultado de prueba
        resultado = new Resultado();
        resultado.setIdResultado(1L);
        resultado.setIdCita(1L);
        resultado.setIdLaboratorista(3L);
        resultado.setArchivoPdf("/resultados/2025/resultado_1.pdf");
        resultado.setObservaciones("Resultado normal");
        resultado.setFechaResultado(LocalDateTime.now());
        resultado.setEstado("COMPLETADO");
        resultado.setValoresMedidos("{\"hemoglobina\": \"14.5 g/dL\"}");
        resultado.setFechaCreacion(LocalDateTime.now());

        // Configurar request de creación
        crearRequest = new CrearResultadoRequest();
        crearRequest.setIdCita(1L);
        crearRequest.setIdLaboratorista(3L);
        crearRequest.setObservaciones("Resultado normal");
        crearRequest.setEstado("PENDIENTE");
        crearRequest.setValoresMedidos("{\"hemoglobina\": \"14.5 g/dL\"}");

        // Configurar request de actualización
        actualizarRequest = new ActualizarResultadoRequest();
        actualizarRequest.setEstado("COMPLETADO");
        actualizarRequest.setObservaciones("Resultado actualizado");
    }

    @Test
    @DisplayName("Debe crear resultado exitosamente")
    void testCrearResultado_Exitoso() {
        // Given
        when(resultadoRepository.existsByIdCita(1L)).thenReturn(false);
        when(resultadoRepository.save(any(Resultado.class))).thenReturn(resultado);

        // When
        ResultadoDTO resultadoDTO = resultadoService.crearResultado(crearRequest);

        // Then
        assertNotNull(resultadoDTO);
        assertEquals(1L, resultadoDTO.getIdCita());
        assertEquals(3L, resultadoDTO.getIdLaboratorista());
        verify(resultadoRepository, times(1)).existsByIdCita(1L);
        verify(resultadoRepository, times(1)).save(any(Resultado.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si ya existe resultado para la cita")
    void testCrearResultado_CitaDuplicada() {
        // Given
        when(resultadoRepository.existsByIdCita(1L)).thenReturn(true);

        // When & Then
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> resultadoService.crearResultado(crearRequest)
        );
        assertTrue(exception.getMessage().contains("Ya existe un resultado"));
        verify(resultadoRepository, times(1)).existsByIdCita(1L);
        verify(resultadoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe obtener resultado por ID exitosamente")
    void testObtenerPorId_Exitoso() {
        // Given
        when(resultadoRepository.findById(1L)).thenReturn(Optional.of(resultado));

        // When
        ResultadoDTO resultadoDTO = resultadoService.obtenerPorId(1L);

        // Then
        assertNotNull(resultadoDTO);
        assertEquals(1L, resultadoDTO.getIdResultado());
        assertEquals("COMPLETADO", resultadoDTO.getEstado());
        verify(resultadoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción si no encuentra resultado por ID")
    void testObtenerPorId_NoEncontrado() {
        // Given
        when(resultadoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
            ResourceNotFoundException.class,
            () -> resultadoService.obtenerPorId(999L)
        );
        verify(resultadoRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debe obtener resultado por ID de cita")
    void testObtenerPorIdCita_Exitoso() {
        // Given
        when(resultadoRepository.findByIdCita(1L)).thenReturn(Optional.of(resultado));

        // When
        ResultadoDTO resultadoDTO = resultadoService.obtenerPorIdCita(1L);

        // Then
        assertNotNull(resultadoDTO);
        assertEquals(1L, resultadoDTO.getIdCita());
        verify(resultadoRepository, times(1)).findByIdCita(1L);
    }

    @Test
    @DisplayName("Debe listar todos los resultados")
    void testListarTodos() {
        // Given
        List<Resultado> resultados = Arrays.asList(resultado);
        when(resultadoRepository.findAllOrderByFechaDesc()).thenReturn(resultados);

        // When
        List<ResultadoDTO> resultadosDTO = resultadoService.listarTodos();

        // Then
        assertNotNull(resultadosDTO);
        assertEquals(1, resultadosDTO.size());
        verify(resultadoRepository, times(1)).findAllOrderByFechaDesc();
    }

    @Test
    @DisplayName("Debe listar resultados por laboratorista")
    void testListarPorLaboratorista() {
        // Given
        List<Resultado> resultados = Arrays.asList(resultado);
        when(resultadoRepository.findByIdLaboratorista(3L)).thenReturn(resultados);

        // When
        List<ResultadoDTO> resultadosDTO = resultadoService.listarPorLaboratorista(3L);

        // Then
        assertNotNull(resultadosDTO);
        assertEquals(1, resultadosDTO.size());
        assertEquals(3L, resultadosDTO.get(0).getIdLaboratorista());
        verify(resultadoRepository, times(1)).findByIdLaboratorista(3L);
    }

    @Test
    @DisplayName("Debe listar resultados por estado")
    void testListarPorEstado() {
        // Given
        List<Resultado> resultados = Arrays.asList(resultado);
        when(resultadoRepository.findByEstado("COMPLETADO")).thenReturn(resultados);

        // When
        List<ResultadoDTO> resultadosDTO = resultadoService.listarPorEstado("COMPLETADO");

        // Then
        assertNotNull(resultadosDTO);
        assertEquals(1, resultadosDTO.size());
        assertEquals("COMPLETADO", resultadosDTO.get(0).getEstado());
        verify(resultadoRepository, times(1)).findByEstado("COMPLETADO");
    }

    @Test
    @DisplayName("Debe actualizar resultado exitosamente")
    void testActualizarResultado_Exitoso() {
        // Given
        when(resultadoRepository.findById(1L)).thenReturn(Optional.of(resultado));
        when(resultadoRepository.save(any(Resultado.class))).thenReturn(resultado);

        // When
        ResultadoDTO resultadoDTO = resultadoService.actualizarResultado(1L, actualizarRequest);

        // Then
        assertNotNull(resultadoDTO);
        verify(resultadoRepository, times(1)).findById(1L);
        verify(resultadoRepository, times(1)).save(any(Resultado.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar resultado no existente")
    void testActualizarResultado_NoEncontrado() {
        // Given
        when(resultadoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
            ResourceNotFoundException.class,
            () -> resultadoService.actualizarResultado(999L, actualizarRequest)
        );
        verify(resultadoRepository, times(1)).findById(999L);
        verify(resultadoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe cambiar estado de resultado exitosamente")
    void testCambiarEstado_Exitoso() {
        // Given
        when(resultadoRepository.findById(1L)).thenReturn(Optional.of(resultado));
        when(resultadoRepository.save(any(Resultado.class))).thenReturn(resultado);

        // When
        ResultadoDTO resultadoDTO = resultadoService.cambiarEstado(1L, "REVISADO");

        // Then
        assertNotNull(resultadoDTO);
        verify(resultadoRepository, times(1)).findById(1L);
        verify(resultadoRepository, times(1)).save(any(Resultado.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si estado no es válido")
    void testCambiarEstado_EstadoInvalido() {
        // Given
        when(resultadoRepository.findById(1L)).thenReturn(Optional.of(resultado));

        // When & Then
        assertThrows(
            BadRequestException.class,
            () -> resultadoService.cambiarEstado(1L, "ESTADO_INVALIDO")
        );
        verify(resultadoRepository, times(1)).findById(1L);
        verify(resultadoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar resultado exitosamente")
    void testEliminarResultado_Exitoso() {
        // Given
        when(resultadoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(resultadoRepository).deleteById(1L);

        // When
        resultadoService.eliminarResultado(1L);

        // Then
        verify(resultadoRepository, times(1)).existsById(1L);
        verify(resultadoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar resultado no existente")
    void testEliminarResultado_NoEncontrado() {
        // Given
        when(resultadoRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(
            ResourceNotFoundException.class,
            () -> resultadoService.eliminarResultado(999L)
        );
        verify(resultadoRepository, times(1)).existsById(999L);
        verify(resultadoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Debe usar estado por defecto al crear resultado sin estado")
    void testCrearResultado_EstadoPorDefecto() {
        // Given
        crearRequest.setEstado(null); // Sin estado
        when(resultadoRepository.existsByIdCita(1L)).thenReturn(false);
        when(resultadoRepository.save(any(Resultado.class))).thenAnswer(invocation -> {
            Resultado r = invocation.getArgument(0);
            r.setIdResultado(1L);
            return r;
        });

        // When
        ResultadoDTO resultadoDTO = resultadoService.crearResultado(crearRequest);

        // Then
        assertNotNull(resultadoDTO);
        verify(resultadoRepository, times(1)).save(argThat(r -> 
            r.getEstado().equals("PENDIENTE")
        ));
    }
}


