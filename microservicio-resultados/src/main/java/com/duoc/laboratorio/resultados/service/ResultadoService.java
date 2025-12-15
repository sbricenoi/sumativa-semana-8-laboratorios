package com.duoc.laboratorio.resultados.service;

import com.duoc.laboratorio.resultados.dto.ActualizarResultadoRequest;
import com.duoc.laboratorio.resultados.dto.CrearResultadoRequest;
import com.duoc.laboratorio.resultados.dto.ResultadoDTO;
import com.duoc.laboratorio.resultados.exception.BadRequestException;
import com.duoc.laboratorio.resultados.exception.ResourceNotFoundException;
import com.duoc.laboratorio.resultados.model.Resultado;
import com.duoc.laboratorio.resultados.repository.ResultadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de lógica de negocio para Resultados de Análisis
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ResultadoService {

    private final ResultadoRepository resultadoRepository;

    /**
     * Crear un nuevo resultado
     */
    public ResultadoDTO crearResultado(CrearResultadoRequest request) {
        // Validar que no exista ya un resultado para esa cita
        if (resultadoRepository.existsByIdCita(request.getIdCita())) {
            throw new BadRequestException("Ya existe un resultado para la cita ID: " + request.getIdCita());
        }

        Resultado resultado = new Resultado();
        resultado.setIdCita(request.getIdCita());
        resultado.setIdLaboratorista(request.getIdLaboratorista());
        resultado.setArchivoPdf(request.getArchivoPdf());
        resultado.setObservaciones(request.getObservaciones());
        resultado.setFechaResultado(request.getFechaResultado() != null ? 
            request.getFechaResultado() : LocalDateTime.now());
        resultado.setEstado(request.getEstado() != null ? request.getEstado() : "PENDIENTE");
        resultado.setValoresMedidos(request.getValoresMedidos());

        Resultado savedResultado = resultadoRepository.save(resultado);
        return convertToDTO(savedResultado);
    }

    /**
     * Obtener resultado por ID
     */
    @Transactional(readOnly = true)
    public ResultadoDTO obtenerPorId(Long id) {
        Resultado resultado = resultadoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Resultado", id));
        return convertToDTO(resultado);
    }

    /**
     * Obtener resultado por ID de cita
     */
    @Transactional(readOnly = true)
    public ResultadoDTO obtenerPorIdCita(Long idCita) {
        Resultado resultado = resultadoRepository.findByIdCita(idCita)
            .orElseThrow(() -> new ResourceNotFoundException("Resultado de la cita " + idCita + " no encontrado"));
        return convertToDTO(resultado);
    }

    /**
     * Listar todos los resultados
     */
    @Transactional(readOnly = true)
    public List<ResultadoDTO> listarTodos() {
        return resultadoRepository.findAllOrderByFechaDesc().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Listar resultados por laboratorista
     */
    @Transactional(readOnly = true)
    public List<ResultadoDTO> listarPorLaboratorista(Long idLaboratorista) {
        return resultadoRepository.findByIdLaboratorista(idLaboratorista).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Listar resultados por estado
     */
    @Transactional(readOnly = true)
    public List<ResultadoDTO> listarPorEstado(String estado) {
        return resultadoRepository.findByEstado(estado).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Actualizar un resultado
     */
    public ResultadoDTO actualizarResultado(Long id, ActualizarResultadoRequest request) {
        Resultado resultado = resultadoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Resultado", id));

        if (request.getArchivoPdf() != null) {
            resultado.setArchivoPdf(request.getArchivoPdf());
        }
        if (request.getObservaciones() != null) {
            resultado.setObservaciones(request.getObservaciones());
        }
        if (request.getFechaResultado() != null) {
            resultado.setFechaResultado(request.getFechaResultado());
        }
        if (request.getEstado() != null) {
            resultado.setEstado(request.getEstado());
        }
        if (request.getValoresMedidos() != null) {
            resultado.setValoresMedidos(request.getValoresMedidos());
        }

        Resultado updatedResultado = resultadoRepository.save(resultado);
        return convertToDTO(updatedResultado);
    }

    /**
     * Cambiar estado de un resultado
     */
    public ResultadoDTO cambiarEstado(Long id, String nuevoEstado) {
        Resultado resultado = resultadoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Resultado", id));

        // Validar estados permitidos
        if (!List.of("PENDIENTE", "EN_PROCESO", "COMPLETADO", "REVISADO").contains(nuevoEstado)) {
            throw new BadRequestException("Estado no válido: " + nuevoEstado);
        }

        resultado.setEstado(nuevoEstado);
        Resultado updatedResultado = resultadoRepository.save(resultado);
        return convertToDTO(updatedResultado);
    }

    /**
     * Eliminar un resultado
     */
    public void eliminarResultado(Long id) {
        if (!resultadoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resultado", id);
        }
        resultadoRepository.deleteById(id);
    }

    /**
     * Convertir entidad a DTO
     */
    private ResultadoDTO convertToDTO(Resultado resultado) {
        ResultadoDTO dto = new ResultadoDTO();
        dto.setIdResultado(resultado.getIdResultado());
        dto.setIdCita(resultado.getIdCita());
        dto.setIdLaboratorista(resultado.getIdLaboratorista());
        dto.setArchivoPdf(resultado.getArchivoPdf());
        dto.setObservaciones(resultado.getObservaciones());
        dto.setFechaResultado(resultado.getFechaResultado());
        dto.setEstado(resultado.getEstado());
        dto.setValoresMedidos(resultado.getValoresMedidos());
        dto.setFechaCreacion(resultado.getFechaCreacion());
        return dto;
    }
}

