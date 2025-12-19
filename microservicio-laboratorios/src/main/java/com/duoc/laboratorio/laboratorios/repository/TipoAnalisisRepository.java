package com.duoc.laboratorio.laboratorios.repository;

import com.duoc.laboratorio.laboratorios.model.TipoAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones de base de datos de TipoAnalisis
 */
@Repository
public interface TipoAnalisisRepository extends JpaRepository<TipoAnalisis, Long> {

    /**
     * Buscar tipos de análisis activos
     */
    List<TipoAnalisis> findByActivo(Integer activo);

    /**
     * Buscar por nombre (búsqueda parcial)
     */
    @Query("SELECT t FROM TipoAnalisis t WHERE LOWER(t.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<TipoAnalisis> buscarPorNombre(@Param("nombre") String nombre);

    /**
     * Buscar análisis por rango de precio
     */
    @Query("SELECT t FROM TipoAnalisis t WHERE t.precio BETWEEN :precioMin AND :precioMax AND t.activo = 1")
    List<TipoAnalisis> findByRangoPrecio(@Param("precioMin") Double precioMin, @Param("precioMax") Double precioMax);
}


