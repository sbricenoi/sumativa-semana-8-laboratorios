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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que contiene la lógica de negocio para gestión de laboratorios
 */
@Service
@Transactional
public class LaboratorioService {

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    @Autowired
    private TipoAnalisisRepository tipoAnalisisRepository;

    @Autowired
    private LaboratorioAnalisisRepository laboratorioAnalisisRepository;

    /**
     * Crear nuevo laboratorio
     */
    public LaboratorioDTO crearLaboratorio(LaboratorioDTO dto) {
        // Validar que el email no exista
        if (laboratorioRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        Laboratorio laboratorio = new Laboratorio();
        laboratorio.setNombre(dto.getNombre());
        laboratorio.setDireccion(dto.getDireccion());
        laboratorio.setTelefono(dto.getTelefono());
        laboratorio.setEmail(dto.getEmail());
        laboratorio.setEspecialidad(dto.getEspecialidad());
        laboratorio.setActivo(1);

        Laboratorio guardado = laboratorioRepository.save(laboratorio);
        return convertirADTO(guardado);
    }

    /**
     * Obtener todos los laboratorios
     */
    @Transactional(readOnly = true)
    public List<LaboratorioDTO> obtenerTodos() {
        return laboratorioRepository.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener laboratorio por ID
     */
    @Transactional(readOnly = true)
    public LaboratorioDTO obtenerPorId(Long id) {
        Laboratorio laboratorio = laboratorioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Laboratorio no encontrado con ID: " + id));
        return convertirADTO(laboratorio);
    }

    /**
     * Obtener laboratorios activos
     */
    @Transactional(readOnly = true)
    public List<LaboratorioDTO> obtenerActivos() {
        return laboratorioRepository.findByActivo(1).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener laboratorios por especialidad
     */
    @Transactional(readOnly = true)
    public List<LaboratorioDTO> obtenerPorEspecialidad(String especialidad) {
        return laboratorioRepository.findByEspecialidad(especialidad).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Buscar laboratorios por nombre
     */
    @Transactional(readOnly = true)
    public List<LaboratorioDTO> buscarPorNombre(String nombre) {
        return laboratorioRepository.buscarPorNombre(nombre).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Actualizar laboratorio
     */
    public LaboratorioDTO actualizarLaboratorio(Long id, LaboratorioDTO dto) {
        Laboratorio laboratorio = laboratorioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Laboratorio no encontrado con ID: " + id));

        // Validar email si cambió
        if (!laboratorio.getEmail().equals(dto.getEmail()) && 
            laboratorioRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        laboratorio.setNombre(dto.getNombre());
        laboratorio.setDireccion(dto.getDireccion());
        laboratorio.setTelefono(dto.getTelefono());
        laboratorio.setEmail(dto.getEmail());
        laboratorio.setEspecialidad(dto.getEspecialidad());
        laboratorio.setActivo(dto.getActivo());

        Laboratorio actualizado = laboratorioRepository.save(laboratorio);
        return convertirADTO(actualizado);
    }

    /**
     * Eliminar laboratorio (borrado lógico)
     */
    public void eliminarLaboratorio(Long id) {
        Laboratorio laboratorio = laboratorioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Laboratorio no encontrado con ID: " + id));
        
        laboratorio.setActivo(0);
        laboratorioRepository.save(laboratorio);
    }

    /**
     * Asignar análisis a laboratorio
     */
    public void asignarAnalisis(Long idLaboratorio, AsignarAnalisisRequest request) {
        // Validar que el laboratorio exista
        if (!laboratorioRepository.existsById(idLaboratorio)) {
            throw new ResourceNotFoundException("Laboratorio no encontrado con ID: " + idLaboratorio);
        }

        // Validar que el tipo de análisis exista
        if (!tipoAnalisisRepository.existsById(request.getIdTipoAnalisis())) {
            throw new ResourceNotFoundException("Tipo de análisis no encontrado con ID: " + request.getIdTipoAnalisis());
        }

        // Verificar si ya existe la asignación
        if (laboratorioAnalisisRepository.existsByIdLaboratorioAndIdTipoAnalisis(
                idLaboratorio, request.getIdTipoAnalisis())) {
            throw new BadRequestException("El análisis ya está asignado a este laboratorio");
        }

        // Crear asignación
        LaboratorioAnalisis asignacion = new LaboratorioAnalisis();
        asignacion.setIdLaboratorio(idLaboratorio);
        asignacion.setIdTipoAnalisis(request.getIdTipoAnalisis());
        asignacion.setDisponible(request.getDisponible() != null ? request.getDisponible() : 1);

        laboratorioAnalisisRepository.save(asignacion);
    }

    /**
     * Obtener análisis asignados a un laboratorio
     */
    @Transactional(readOnly = true)
    public List<LaboratorioAnalisis> obtenerAnalisisAsignados(Long idLaboratorio) {
        if (!laboratorioRepository.existsById(idLaboratorio)) {
            throw new ResourceNotFoundException("Laboratorio no encontrado con ID: " + idLaboratorio);
        }
        return laboratorioAnalisisRepository.findByIdLaboratorio(idLaboratorio);
    }

    /**
     * Convertir entidad a DTO
     */
    private LaboratorioDTO convertirADTO(Laboratorio laboratorio) {
        LaboratorioDTO dto = new LaboratorioDTO();
        dto.setIdLaboratorio(laboratorio.getIdLaboratorio());
        dto.setNombre(laboratorio.getNombre());
        dto.setDireccion(laboratorio.getDireccion());
        dto.setTelefono(laboratorio.getTelefono());
        dto.setEmail(laboratorio.getEmail());
        dto.setEspecialidad(laboratorio.getEspecialidad());
        dto.setActivo(laboratorio.getActivo());
        dto.setFechaCreacion(laboratorio.getFechaCreacion());
        return dto;
    }
}


