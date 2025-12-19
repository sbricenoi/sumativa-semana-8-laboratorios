package com.duoc.laboratorio.laboratorios.repository;

import com.duoc.laboratorio.laboratorios.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para operaciones de base de datos de Cita
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    /**
     * Buscar citas por paciente
     */
    List<Cita> findByIdPaciente(Long idPaciente);

    /**
     * Buscar citas por laboratorio
     */
    List<Cita> findByIdLaboratorio(Long idLaboratorio);

    /**
     * Buscar citas por estado
     */
    List<Cita> findByEstado(String estado);

    /**
     * Buscar citas por laboratorio y estado
     */
    List<Cita> findByIdLaboratorioAndEstado(Long idLaboratorio, String estado);

    /**
     * Buscar citas por paciente y estado
     */
    List<Cita> findByIdPacienteAndEstado(Long idPaciente, String estado);

    /**
     * Buscar citas por rango de fechas
     */
    @Query("SELECT c FROM Cita c WHERE c.fechaCita BETWEEN :fechaInicio AND :fechaFin")
    List<Cita> findByRangoFechas(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                   @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Buscar citas próximas por laboratorio
     */
    @Query("SELECT c FROM Cita c WHERE c.idLaboratorio = :idLaboratorio " +
           "AND c.fechaCita >= CURRENT_TIMESTAMP " +
           "ORDER BY c.fechaCita ASC")
    List<Cita> findProximasCitasPorLaboratorio(@Param("idLaboratorio") Long idLaboratorio);
}


