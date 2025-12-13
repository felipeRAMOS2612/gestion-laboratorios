package com.gestion_laboratorios.asignacion_lab.repository;

import com.gestion_laboratorios.asignacion_lab.entity.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    List<Asignacion> findByUsuarioId(Long usuarioId);
    
    List<Asignacion> findByUsuarioIdOrderByFechaHoraInicioDesc(Long usuarioId);
    
    List<Asignacion> findByLaboratorioId(Long laboratorioId);
    
    List<Asignacion> findByTipoAnalisisId(Long tipoAnalisisId);
    
    List<Asignacion> findByEstado(Asignacion.EstadoAsignacion estado);
    
    List<Asignacion> findByEstadoOrderByFechaHoraInicio(Asignacion.EstadoAsignacion estado);
    
    @Query("SELECT a FROM Asignacion a WHERE a.laboratorio.id = :laboratorioId AND " +
           "a.fechaHoraInicio BETWEEN :fechaInicio AND :fechaFin AND " +
           "a.estado IN (com.gestion_laboratorios.asignacion_lab.entity.Asignacion.EstadoAsignacion.PROGRAMADA, " +
           "com.gestion_laboratorios.asignacion_lab.entity.Asignacion.EstadoAsignacion.EN_PROGRESO)")
    List<Asignacion> findConflictosHorario(
        @Param("laboratorioId") Long laboratorioId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
    );
    
    @Query("SELECT a FROM Asignacion a WHERE " +
           "a.fechaHoraInicio BETWEEN :fechaInicio AND :fechaFin")
    List<Asignacion> findByFechaHoraInicioBetween(
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
    );
    
    @Query("SELECT a FROM Asignacion a WHERE " +
           "a.usuarioId = :usuarioId AND a.estado = :estado")
    List<Asignacion> findByUsuarioIdAndEstado(
        @Param("usuarioId") Long usuarioId,
        @Param("estado") Asignacion.EstadoAsignacion estado
    );
    
    @Query("SELECT a FROM Asignacion a WHERE " +
           "a.laboratorio.id = :laboratorioId AND " +
           "a.estado IN (com.gestion_laboratorios.asignacion_lab.entity.Asignacion.EstadoAsignacion.PROGRAMADA, " +
           "com.gestion_laboratorios.asignacion_lab.entity.Asignacion.EstadoAsignacion.EN_PROGRESO) AND " +
           "a.fechaHoraInicio >= :fechaActual")
    List<Asignacion> findAsignacionesActivasByLaboratorio(
        @Param("laboratorioId") Long laboratorioId,
        @Param("fechaActual") LocalDateTime fechaActual
    );
    
    @Query("SELECT a FROM Asignacion a WHERE " +
           "LOWER(a.nombrePaciente) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Asignacion> findByNombrePacienteContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(a) FROM Asignacion a WHERE " +
           "a.laboratorio.id = :laboratorioId AND " +
           "DATE(a.fechaHoraInicio) = DATE(:fecha) AND " +
           "a.estado != com.gestion_laboratorios.asignacion_lab.entity.Asignacion.EstadoAsignacion.CANCELADA")
    long countAsignacionesByLaboratorioAndFecha(
        @Param("laboratorioId") Long laboratorioId,
        @Param("fecha") LocalDateTime fecha
    );
}