package com.duoc.laboratorio.resultados.repository;

import com.duoc.laboratorio.resultados.model.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestión de Resultados de Análisis
 */
@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
    
    /**
     * Buscar resultado por ID de cita
     */
    Optional<Resultado> findByIdCita(Long idCita);
    
    /**
     * Verificar si existe un resultado para una cita
     */
    boolean existsByIdCita(Long idCita);
    
    /**
     * Buscar todos los resultados de un laboratorista
     */
    List<Resultado> findByIdLaboratorista(Long idLaboratorista);
    
    /**
     * Buscar resultados por estado
     */
    List<Resultado> findByEstado(String estado);
    
    /**
     * Buscar resultados por estado y laboratorista
     */
    List<Resultado> findByEstadoAndIdLaboratorista(String estado, Long idLaboratorista);
    
    /**
     * Obtener resultados ordenados por fecha descendente
     */
    @Query("SELECT r FROM Resultado r ORDER BY r.fechaResultado DESC")
    List<Resultado> findAllOrderByFechaDesc();
}

