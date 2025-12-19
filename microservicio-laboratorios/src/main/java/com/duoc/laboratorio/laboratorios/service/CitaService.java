package com.duoc.laboratorio.laboratorios.service;

import com.duoc.laboratorio.laboratorios.dto.CitaDTO;
import com.duoc.laboratorio.laboratorios.exception.BadRequestException;
import com.duoc.laboratorio.laboratorios.exception.ResourceNotFoundException;
import com.duoc.laboratorio.laboratorios.model.Cita;
import com.duoc.laboratorio.laboratorios.repository.CitaRepository;
import com.duoc.laboratorio.laboratorios.repository.LaboratorioRepository;
import com.duoc.laboratorio.laboratorios.repository.TipoAnalisisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que contiene la lógica de negocio para gestión de citas
 */
@Service
@Transactional
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    @Autowired
    private TipoAnalisisRepository tipoAnalisisRepository;

    /**
     * Crear nueva cita
     */
    public CitaDTO crearCita(CitaDTO dto) {
        // Validar que el laboratorio exista
        if (!laboratorioRepository.existsById(dto.getIdLaboratorio())) {
            throw new ResourceNotFoundException("Laboratorio no encontrado con ID: " + dto.getIdLaboratorio());
        }

        // Validar que el tipo de análisis exista
        if (!tipoAnalisisRepository.existsById(dto.getIdTipoAnalisis())) {
            throw new ResourceNotFoundException("Tipo de análisis no encontrado con ID: " + dto.getIdTipoAnalisis());
        }

        // Validar que la fecha sea futura
        if (dto.getFechaCita().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("La fecha de la cita debe ser futura");
        }

        Cita cita = new Cita();
        cita.setIdPaciente(dto.getIdPaciente());
        cita.setIdLaboratorio(dto.getIdLaboratorio());
        cita.setIdTipoAnalisis(dto.getIdTipoAnalisis());
        cita.setFechaCita(dto.getFechaCita());
        cita.setEstado(dto.getEstado() != null ? dto.getEstado() : "PROGRAMADA");
        cita.setObservaciones(dto.getObservaciones());

        Cita guardada = citaRepository.save(cita);
        return convertirADTO(guardada);
    }

    /**
     * Obtener todas las citas
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerTodas() {
        return citaRepository.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener cita por ID
     */
    @Transactional(readOnly = true)
    public CitaDTO obtenerPorId(Long id) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));
        return convertirADTO(cita);
    }

    /**
     * Obtener citas por paciente
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerPorPaciente(Long idPaciente) {
        return citaRepository.findByIdPaciente(idPaciente).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener citas por laboratorio
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerPorLaboratorio(Long idLaboratorio) {
        return citaRepository.findByIdLaboratorio(idLaboratorio).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener citas por estado
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerPorEstado(String estado) {
        return citaRepository.findByEstado(estado).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener próximas citas por laboratorio
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerProximasCitas(Long idLaboratorio) {
        return citaRepository.findProximasCitasPorLaboratorio(idLaboratorio).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Actualizar cita
     */
    public CitaDTO actualizarCita(Long id, CitaDTO dto) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));

        cita.setIdPaciente(dto.getIdPaciente());
        cita.setIdLaboratorio(dto.getIdLaboratorio());
        cita.setIdTipoAnalisis(dto.getIdTipoAnalisis());
        cita.setFechaCita(dto.getFechaCita());
        cita.setEstado(dto.getEstado());
        cita.setObservaciones(dto.getObservaciones());

        Cita actualizada = citaRepository.save(cita);
        return convertirADTO(actualizada);
    }

    /**
     * Cambiar estado de cita
     */
    public CitaDTO cambiarEstado(Long id, String nuevoEstado) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));

        // Validar estado válido
        if (!List.of("PROGRAMADA", "CONFIRMADA", "COMPLETADA", "CANCELADA").contains(nuevoEstado)) {
            throw new BadRequestException("Estado inválido: " + nuevoEstado);
        }

        cita.setEstado(nuevoEstado);
        Cita actualizada = citaRepository.save(cita);
        return convertirADTO(actualizada);
    }

    /**
     * Eliminar cita
     */
    public void eliminarCita(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita no encontrada con ID: " + id);
        }
        citaRepository.deleteById(id);
    }

    /**
     * Convertir entidad a DTO
     */
    private CitaDTO convertirADTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setIdCita(cita.getIdCita());
        dto.setIdPaciente(cita.getIdPaciente());
        dto.setIdLaboratorio(cita.getIdLaboratorio());
        dto.setIdTipoAnalisis(cita.getIdTipoAnalisis());
        dto.setFechaCita(cita.getFechaCita());
        dto.setEstado(cita.getEstado());
        dto.setObservaciones(cita.getObservaciones());
        dto.setFechaCreacion(cita.getFechaCreacion());
        return dto;
    }
}


