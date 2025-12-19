package com.duoc.laboratorio.laboratorios.service;

import com.duoc.laboratorio.laboratorios.dto.TipoAnalisisDTO;
import com.duoc.laboratorio.laboratorios.exception.ResourceNotFoundException;
import com.duoc.laboratorio.laboratorios.model.TipoAnalisis;
import com.duoc.laboratorio.laboratorios.repository.TipoAnalisisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para TipoAnalisisService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de TipoAnalisisService")
class TipoAnalisisServiceTest {

    @Mock
    private TipoAnalisisRepository tipoAnalisisRepository;

    @InjectMocks
    private TipoAnalisisService tipoAnalisisService;

    private TipoAnalisis tipoAnalisis;
    private TipoAnalisisDTO tipoAnalisisDTO;

    @BeforeEach
    void setUp() {
        // Configurar tipo de análisis de prueba
        tipoAnalisis = new TipoAnalisis();
        tipoAnalisis.setIdTipoAnalisis(1L);
        tipoAnalisis.setNombre("Hemograma Completo");
        tipoAnalisis.setDescripcion("Análisis completo de sangre");
        tipoAnalisis.setPrecio(new BigDecimal("25000.00"));
        tipoAnalisis.setTiempoEntregaDias(2);
        tipoAnalisis.setActivo(1);
        tipoAnalisis.setFechaCreacion(LocalDateTime.now());

        // Configurar DTO de prueba
        tipoAnalisisDTO = new TipoAnalisisDTO();
        tipoAnalisisDTO.setNombre("Hemograma Completo");
        tipoAnalisisDTO.setDescripcion("Análisis completo de sangre");
        tipoAnalisisDTO.setPrecio(new BigDecimal("25000.00"));
        tipoAnalisisDTO.setTiempoEntregaDias(2);
        tipoAnalisisDTO.setActivo(1);
    }

    @Test
    @DisplayName("Debe crear tipo de análisis exitosamente")
    void testCrearTipoAnalisis_Exitoso() {
        // Given
        when(tipoAnalisisRepository.save(any(TipoAnalisis.class))).thenReturn(tipoAnalisis);

        // When
        TipoAnalisisDTO resultado = tipoAnalisisService.crearTipoAnalisis(tipoAnalisisDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Hemograma Completo", resultado.getNombre());
        assertEquals(new BigDecimal("25000.00"), resultado.getPrecio());
        verify(tipoAnalisisRepository, times(1)).save(any(TipoAnalisis.class));
    }

    @Test
    @DisplayName("Debe obtener todos los tipos de análisis")
    void testObtenerTodos() {
        // Given
        List<TipoAnalisis> tipos = Arrays.asList(tipoAnalisis);
        when(tipoAnalisisRepository.findAll()).thenReturn(tipos);

        // When
        List<TipoAnalisisDTO> resultado = tipoAnalisisService.obtenerTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Hemograma Completo", resultado.get(0).getNombre());
        verify(tipoAnalisisRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener tipo de análisis por ID exitosamente")
    void testObtenerPorId_Exitoso() {
        // Given
        when(tipoAnalisisRepository.findById(1L)).thenReturn(Optional.of(tipoAnalisis));

        // When
        TipoAnalisisDTO resultado = tipoAnalisisService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdTipoAnalisis());
        assertEquals("Hemograma Completo", resultado.getNombre());
        verify(tipoAnalisisRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción si no encuentra tipo de análisis por ID")
    void testObtenerPorId_NoEncontrado() {
        // Given
        when(tipoAnalisisRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
            ResourceNotFoundException.class,
            () -> tipoAnalisisService.obtenerPorId(999L)
        );
        verify(tipoAnalisisRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debe obtener tipos de análisis activos")
    void testObtenerActivos() {
        // Given
        List<TipoAnalisis> tipos = Arrays.asList(tipoAnalisis);
        when(tipoAnalisisRepository.findByActivo(1)).thenReturn(tipos);

        // When
        List<TipoAnalisisDTO> resultado = tipoAnalisisService.obtenerActivos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getActivo());
        verify(tipoAnalisisRepository, times(1)).findByActivo(1);
    }

    @Test
    @DisplayName("Debe buscar tipos de análisis por nombre")
    void testBuscarPorNombre() {
        // Given
        List<TipoAnalisis> tipos = Arrays.asList(tipoAnalisis);
        when(tipoAnalisisRepository.buscarPorNombre("Hemograma")).thenReturn(tipos);

        // When
        List<TipoAnalisisDTO> resultado = tipoAnalisisService.buscarPorNombre("Hemograma");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getNombre().contains("Hemograma"));
        verify(tipoAnalisisRepository, times(1)).buscarPorNombre("Hemograma");
    }

    @Test
    @DisplayName("Debe actualizar tipo de análisis exitosamente")
    void testActualizarTipoAnalisis_Exitoso() {
        // Given
        when(tipoAnalisisRepository.findById(1L)).thenReturn(Optional.of(tipoAnalisis));
        when(tipoAnalisisRepository.save(any(TipoAnalisis.class))).thenReturn(tipoAnalisis);

        tipoAnalisisDTO.setPrecio(new BigDecimal("30000.00"));

        // When
        TipoAnalisisDTO resultado = tipoAnalisisService.actualizarTipoAnalisis(1L, tipoAnalisisDTO);

        // Then
        assertNotNull(resultado);
        verify(tipoAnalisisRepository, times(1)).findById(1L);
        verify(tipoAnalisisRepository, times(1)).save(any(TipoAnalisis.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar tipo de análisis no existente")
    void testActualizarTipoAnalisis_NoEncontrado() {
        // Given
        when(tipoAnalisisRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
            ResourceNotFoundException.class,
            () -> tipoAnalisisService.actualizarTipoAnalisis(999L, tipoAnalisisDTO)
        );
        verify(tipoAnalisisRepository, times(1)).findById(999L);
        verify(tipoAnalisisRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar tipo de análisis (soft delete)")
    void testEliminarTipoAnalisis() {
        // Given
        when(tipoAnalisisRepository.findById(1L)).thenReturn(Optional.of(tipoAnalisis));
        when(tipoAnalisisRepository.save(any(TipoAnalisis.class))).thenReturn(tipoAnalisis);

        // When
        tipoAnalisisService.eliminarTipoAnalisis(1L);

        // Then
        verify(tipoAnalisisRepository, times(1)).findById(1L);
        verify(tipoAnalisisRepository, times(1)).save(argThat(tipo -> tipo.getActivo() == 0));
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar tipo de análisis no existente")
    void testEliminarTipoAnalisis_NoEncontrado() {
        // Given
        when(tipoAnalisisRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
            ResourceNotFoundException.class,
            () -> tipoAnalisisService.eliminarTipoAnalisis(999L)
        );
        verify(tipoAnalisisRepository, times(1)).findById(999L);
        verify(tipoAnalisisRepository, never()).save(any());
    }
}


