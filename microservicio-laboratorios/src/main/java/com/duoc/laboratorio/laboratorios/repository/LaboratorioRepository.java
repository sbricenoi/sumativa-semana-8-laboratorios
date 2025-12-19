package com.duoc.laboratorio.laboratorios.repository;

import com.duoc.laboratorio.laboratorios.model.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones de base de datos de Laboratorio
 */
@Repository
public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {

    /**
     * Buscar laboratorios activos
     */
    List<Laboratorio> findByActivo(Integer activo);

    /**
     * Buscar laboratorios por especialidad
     */
    List<Laboratorio> findByEspecialidad(String especialidad);

    /**
     * Verificar si existe un email
     */
    boolean existsByEmail(String email);

    /**
     * Buscar laboratorios por nombre (búsqueda parcial)
     */
    @Query("SELECT l FROM Laboratorio l WHERE LOWER(l.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Laboratorio> buscarPorNombre(@Param("nombre") String nombre);

    /**
     * Buscar laboratorios activos por especialidad
     */
    @Query("SELECT l FROM Laboratorio l WHERE l.especialidad = :especialidad AND l.activo = 1")
    List<Laboratorio> findActivosByEspecialidad(@Param("especialidad") String especialidad);
}


