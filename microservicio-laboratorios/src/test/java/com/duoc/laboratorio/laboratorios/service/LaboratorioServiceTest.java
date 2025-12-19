package com.duoc.laboratorio.laboratorios.service;

import com.duoc.laboratorio.laboratorios.dto.AsignarAnalisisRequest;
import com.duoc.laboratorio.laboratorios.dto.LaboratorioDTO;
import com.duoc.laboratorio.laboratorios.exception.BadRequestException;
import com.duoc.laboratorio.laboratorios.exception.ResourceNotFoundException;
import com.duoc.laboratorio.laboratorios.model.Laboratorio;
import com.duoc.laboratorio.laboratorios.model.LaboratorioAnalisis;
import com.duoc.laboratorio.laboratorios.repository.LaboratorioAnalisisRepository;
import com.duoc.laboratorio.laboratorios.repository.LaboratorioRepository;
import com.duoc.laboratorio.laboratorios.repository.TipoAnalisisRepository;
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
 * Tests unitarios para LaboratorioService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de LaboratorioService")
class LaboratorioServiceTest {

    @Mock
    private LaboratorioRepository laboratorioRepository;

    @Mock
    private TipoAnalisisRepository tipoAnalisisRepository;

    @Mock
    private LaboratorioAnalisisRepository laboratorioAnalisisRepository;

    @InjectMocks
    private LaboratorioService laboratorioService;

    private Laboratorio laboratorio;
    private LaboratorioDTO laboratorioDTO;

    @BeforeEach
    void setUp() {
        // Configurar laboratorio de prueba
        laboratorio = new Laboratorio();
        laboratorio.setIdLaboratorio(1L);
        laboratorio.setNombre("Laboratorio Central");
        laboratorio.setDireccion("Av. Principal 123");
        laboratorio.setTelefono("+56912345678");
        laboratorio.setEmail("lab@central.cl");
        laboratorio.setEspecialidad("Análisis General");
        laboratorio.setActivo(1);
        laboratorio.setFechaCreacion(LocalDateTime.now());

        // Configurar DTO de prueba
        laboratorioDTO = new LaboratorioDTO();
        laboratorioDTO.setNombre("Laboratorio Central");
        laboratorioDTO.setDireccion("Av. Principal 123");
        laboratorioDTO.setTelefono("+56912345678");
        laboratorioDTO.setEmail("lab@central.cl");
        laboratorioDTO.setEspecialidad("Análisis General");
        laboratorioDTO.setActivo(1);
    }

    @Test
    @DisplayName("Debe crear laboratorio exitosamente")
    void testCrearLaboratorio_Exitoso() {
        // Given
        when(laboratorioRepository.existsByEmail(anyString())).thenReturn(false);
        when(laboratorioRepository.save(any(Laboratorio.class))).thenReturn(laboratorio);

        // When
        LaboratorioDTO resultado = laboratorioService.crearLaboratorio(laboratorioDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Laboratorio Central", resultado.getNombre());
        assertEquals("lab@central.cl", resultado.getEmail());
        verify(laboratorioRepository, times(1)).existsByEmail(anyString());
        verify(laboratorioRepository, times(1)).save(any(Laboratorio.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el email ya existe al crear")
    void testCrearLaboratorio_EmailDuplicado() {
        // Given
        when(laboratorioRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> laboratorioService.crearLaboratorio(laboratorioDTO)
        );
        assertEquals("El email ya está registrado", exception.getMessage());
        verify(laboratorioRepository, times(1)).existsByEmail(anyString());
        verify(laboratorioRepository, never()).save(any(Laboratorio.class));
    }

    @Test
    @DisplayName("Debe obtener todos los laboratorios")
    void testObtenerTodos() {
        // Given
        List<Laboratorio> laboratorios = Arrays.asList(laboratorio);
        when(laboratorioRepository.findAll()).thenReturn(laboratorios);

        // When
        List<LaboratorioDTO> resultado = laboratorioService.obtenerTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Laboratorio Central", resultado.get(0).getNombre());
        verify(laboratorioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener laboratorio por ID exitosamente")
    void testObtenerPorId_Exitoso() {
        // Given
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorio));

        // When
        LaboratorioDTO resultado = laboratorioService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdLaboratorio());
        assertEquals("Laboratorio Central", resultado.getNombre());
        verify(laboratorioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción si no encuentra laboratorio por ID")
    void testObtenerPorId_NoEncontrado() {
        // Given
        when(laboratorioRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
            ResourceNotFoundException.class,
            () -> laboratorioService.obtenerPorId(999L)
        );
        verify(laboratorioRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debe obtener laboratorios activos")
    void testObtenerActivos() {
        // Given
        List<Laboratorio> laboratorios = Arrays.asList(laboratorio);
        when(laboratorioRepository.findByActivo(1)).thenReturn(laboratorios);

        // When
        List<LaboratorioDTO> resultado = laboratorioService.obtenerActivos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getActivo());
        verify(laboratorioRepository, times(1)).findByActivo(1);
    }

    @Test
    @DisplayName("Debe obtener laboratorios por especialidad")
    void testObtenerPorEspecialidad() {
        // Given
        List<Laboratorio> laboratorios = Arrays.asList(laboratorio);
        when(laboratorioRepository.findByEspecialidad("Análisis General")).thenReturn(laboratorios);

        // When
        List<LaboratorioDTO> resultado = laboratorioService.obtenerPorEspecialidad("Análisis General");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Análisis General", resultado.get(0).getEspecialidad());
        verify(laboratorioRepository, times(1)).findByEspecialidad("Análisis General");
    }

    @Test
    @DisplayName("Debe buscar laboratorios por nombre")
    void testBuscarPorNombre() {
        // Given
        List<Laboratorio> laboratorios = Arrays.asList(laboratorio);
        when(laboratorioRepository.buscarPorNombre("Central")).thenReturn(laboratorios);

        // When
        List<LaboratorioDTO> resultado = laboratorioService.buscarPorNombre("Central");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getNombre().contains("Central"));
        verify(laboratorioRepository, times(1)).buscarPorNombre("Central");
    }

    @Test
    @DisplayName("Debe actualizar laboratorio exitosamente")
    void testActualizarLaboratorio_Exitoso() {
        // Given
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorio));
        when(laboratorioRepository.save(any(Laboratorio.class))).thenReturn(laboratorio);

        laboratorioDTO.setNombre("Laboratorio Central Actualizado");

        // When
        LaboratorioDTO resultado = laboratorioService.actualizarLaboratorio(1L, laboratorioDTO);

        // Then
        assertNotNull(resultado);
        verify(laboratorioRepository, times(1)).findById(1L);
        verify(laboratorioRepository, times(1)).save(any(Laboratorio.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si email ya existe al actualizar")
    void testActualizarLaboratorio_EmailDuplicado() {
        // Given
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorio));
        when(laboratorioRepository.existsByEmail("otro@email.cl")).thenReturn(true);

        laboratorioDTO.setEmail("otro@email.cl");

        // When & Then
        assertThrows(
            BadRequestException.class,
            () -> laboratorioService.actualizarLaboratorio(1L, laboratorioDTO)
        );
        verify(laboratorioRepository, times(1)).findById(1L);
        verify(laboratorioRepository, never()).save(any(Laboratorio.class));
    }

    @Test
    @DisplayName("Debe eliminar laboratorio (soft delete)")
    void testEliminarLaboratorio() {
        // Given
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorio));
        when(laboratorioRepository.save(any(Laboratorio.class))).thenReturn(laboratorio);

        // When
        laboratorioService.eliminarLaboratorio(1L);

        // Then
        verify(laboratorioRepository, times(1)).findById(1L);
        verify(laboratorioRepository, times(1)).save(argThat(lab -> lab.getActivo() == 0));
    }

    @Test
    @DisplayName("Debe asignar análisis a laboratorio exitosamente")
    void testAsignarAnalisis_Exitoso() {
        // Given
        AsignarAnalisisRequest request = new AsignarAnalisisRequest();
        request.setIdTipoAnalisis(1L);
        request.setDisponible(1);

        when(laboratorioRepository.existsById(1L)).thenReturn(true);
        when(tipoAnalisisRepository.existsById(1L)).thenReturn(true);
        when(laboratorioAnalisisRepository.existsByIdLaboratorioAndIdTipoAnalisis(1L, 1L)).thenReturn(false);
        when(laboratorioAnalisisRepository.save(any(LaboratorioAnalisis.class))).thenReturn(new LaboratorioAnalisis());

        // When
        laboratorioService.asignarAnalisis(1L, request);

        // Then
        verify(laboratorioRepository, times(1)).existsById(1L);
        verify(tipoAnalisisRepository, times(1)).existsById(1L);
        verify(laboratorioAnalisisRepository, times(1)).save(any(LaboratorioAnalisis.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si laboratorio no existe al asignar análisis")
    void testAsignarAnalisis_LaboratorioNoExiste() {
        // Given
        AsignarAnalisisRequest request = new AsignarAnalisisRequest();
        request.setIdTipoAnalisis(1L);

        when(laboratorioRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(
            ResourceNotFoundException.class,
            () -> laboratorioService.asignarAnalisis(999L, request)
        );
        verify(laboratorioRepository, times(1)).existsById(999L);
        verify(laboratorioAnalisisRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si análisis ya está asignado")
    void testAsignarAnalisis_YaAsignado() {
        // Given
        AsignarAnalisisRequest request = new AsignarAnalisisRequest();
        request.setIdTipoAnalisis(1L);

        when(laboratorioRepository.existsById(1L)).thenReturn(true);
        when(tipoAnalisisRepository.existsById(1L)).thenReturn(true);
        when(laboratorioAnalisisRepository.existsByIdLaboratorioAndIdTipoAnalisis(1L, 1L)).thenReturn(true);

        // When & Then
        assertThrows(
            BadRequestException.class,
            () -> laboratorioService.asignarAnalisis(1L, request)
        );
        verify(laboratorioAnalisisRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe obtener análisis asignados a laboratorio")
    void testObtenerAnalisisAsignados() {
        // Given
        LaboratorioAnalisis asignacion = new LaboratorioAnalisis();
        asignacion.setIdLaboratorio(1L);
        asignacion.setIdTipoAnalisis(1L);
        
        when(laboratorioRepository.existsById(1L)).thenReturn(true);
        when(laboratorioAnalisisRepository.findByIdLaboratorio(1L))
            .thenReturn(Arrays.asList(asignacion));

        // When
        List<LaboratorioAnalisis> resultado = laboratorioService.obtenerAnalisisAsignados(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(laboratorioRepository, times(1)).existsById(1L);
        verify(laboratorioAnalisisRepository, times(1)).findByIdLaboratorio(1L);
    }
}


