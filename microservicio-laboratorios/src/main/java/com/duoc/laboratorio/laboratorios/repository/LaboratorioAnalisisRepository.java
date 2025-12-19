package com.duoc.laboratorio.laboratorios.repository;

import com.duoc.laboratorio.laboratorios.model.LaboratorioAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones de base de datos de LaboratorioAnalisis
 */
@Repository
public interface LaboratorioAnalisisRepository extends JpaRepository<LaboratorioAnalisis, LaboratorioAnalisis.LaboratorioAnalisisId> {

    /**
     * Buscar asignaciones por laboratorio
     */
    List<LaboratorioAnalisis> findByIdLaboratorio(Long idLaboratorio);

    /**
     * Buscar asignaciones por tipo de análisis
     */
    List<LaboratorioAnalisis> findByIdTipoAnalisis(Long idTipoAnalisis);

    /**
     * Verificar si existe una asignación
     */
    boolean existsByIdLaboratorioAndIdTipoAnalisis(Long idLaboratorio, Long idTipoAnalisis);

    /**
     * Buscar análisis disponibles de un laboratorio
     */
    @Query("SELECT la FROM LaboratorioAnalisis la WHERE la.idLaboratorio = :idLaboratorio AND la.disponible = 1")
    List<LaboratorioAnalisis> findAnalisisDisponiblesPorLaboratorio(@Param("idLaboratorio") Long idLaboratorio);
}


