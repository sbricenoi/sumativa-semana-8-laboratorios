package com.duoc.laboratorio.laboratorios.service;

import com.duoc.laboratorio.laboratorios.dto.TipoAnalisisDTO;
import com.duoc.laboratorio.laboratorios.exception.ResourceNotFoundException;
import com.duoc.laboratorio.laboratorios.model.TipoAnalisis;
import com.duoc.laboratorio.laboratorios.repository.TipoAnalisisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que contiene la lógica de negocio para gestión de tipos de análisis
 */
@Service
@Transactional
public class TipoAnalisisService {

    @Autowired
    private TipoAnalisisRepository tipoAnalisisRepository;

    /**
     * Crear nuevo tipo de análisis
     */
    public TipoAnalisisDTO crearTipoAnalisis(TipoAnalisisDTO dto) {
        TipoAnalisis tipoAnalisis = new TipoAnalisis();
        tipoAnalisis.setNombre(dto.getNombre());
        tipoAnalisis.setDescripcion(dto.getDescripcion());
        tipoAnalisis.setPrecio(dto.getPrecio());
        tipoAnalisis.setTiempoEntregaDias(dto.getTiempoEntregaDias());
        tipoAnalisis.setActivo(1);

        TipoAnalisis guardado = tipoAnalisisRepository.save(tipoAnalisis);
        return convertirADTO(guardado);
    }

    /**
     * Obtener todos los tipos de análisis
     */
    @Transactional(readOnly = true)
    public List<TipoAnalisisDTO> obtenerTodos() {
        return tipoAnalisisRepository.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtener tipo de análisis por ID
     */
    @Transactional(readOnly = true)
    public TipoAnalisisDTO obtenerPorId(Long id) {
        TipoAnalisis tipoAnalisis = tipoAnalisisRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tipo de análisis no encontrado con ID: " + id));
        return convertirADTO(tipoAnalisis);
    }

    /**
     * Obtener tipos de análisis activos
     */
    @Transactional(readOnly = true)
    public List<TipoAnalisisDTO> obtenerActivos() {
        return tipoAnalisisRepository.findByActivo(1).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Buscar por nombre
     */
    @Transactional(readOnly = true)
    public List<TipoAnalisisDTO> buscarPorNombre(String nombre) {
        return tipoAnalisisRepository.buscarPorNombre(nombre).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Actualizar tipo de análisis
     */
    public TipoAnalisisDTO actualizarTipoAnalisis(Long id, TipoAnalisisDTO dto) {
        TipoAnalisis tipoAnalisis = tipoAnalisisRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tipo de análisis no encontrado con ID: " + id));

        tipoAnalisis.setNombre(dto.getNombre());
        tipoAnalisis.setDescripcion(dto.getDescripcion());
        tipoAnalisis.setPrecio(dto.getPrecio());
        tipoAnalisis.setTiempoEntregaDias(dto.getTiempoEntregaDias());
        tipoAnalisis.setActivo(dto.getActivo());

        TipoAnalisis actualizado = tipoAnalisisRepository.save(tipoAnalisis);
        return convertirADTO(actualizado);
    }

    /**
     * Eliminar tipo de análisis (borrado lógico)
     */
    public void eliminarTipoAnalisis(Long id) {
        TipoAnalisis tipoAnalisis = tipoAnalisisRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tipo de análisis no encontrado con ID: " + id));
        
        tipoAnalisis.setActivo(0);
        tipoAnalisisRepository.save(tipoAnalisis);
    }

    /**
     * Convertir entidad a DTO
     */
    private TipoAnalisisDTO convertirADTO(TipoAnalisis tipoAnalisis) {
        TipoAnalisisDTO dto = new TipoAnalisisDTO();
        dto.setIdTipoAnalisis(tipoAnalisis.getIdTipoAnalisis());
        dto.setNombre(tipoAnalisis.getNombre());
        dto.setDescripcion(tipoAnalisis.getDescripcion());
        dto.setPrecio(tipoAnalisis.getPrecio());
        dto.setTiempoEntregaDias(tipoAnalisis.getTiempoEntregaDias());
        dto.setActivo(tipoAnalisis.getActivo());
        dto.setFechaCreacion(tipoAnalisis.getFechaCreacion());
        return dto;
    }
}


