package com.gestion_laboratorios.asignacion_lab.repository;

import com.gestion_laboratorios.asignacion_lab.entity.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {

    List<Laboratorio> findByEstado(Laboratorio.EstadoLaboratorio estado);
    
    List<Laboratorio> findByEstadoOrderByNombre(Laboratorio.EstadoLaboratorio estado);
    
    @Query("SELECT l FROM Laboratorio l WHERE l.estado = :estado AND l.capacidadMaxima >= :capacidadMinima")
    List<Laboratorio> findByEstadoAndCapacidadMinimaGreaterThanEqual(
        @Param("estado") Laboratorio.EstadoLaboratorio estado,
        @Param("capacidadMinima") Integer capacidadMinima
    );
    
    @Query("SELECT l FROM Laboratorio l WHERE " +
           "LOWER(l.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.ubicacion) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.equipamiento) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Laboratorio> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    Optional<Laboratorio> findByNombreIgnoreCase(String nombre);
    
    boolean existsByNombreIgnoreCase(String nombre);
    
    List<Laboratorio> findByUbicacionIgnoreCase(String ubicacion);
    
    @Query("SELECT COUNT(a) FROM Asignacion a WHERE a.laboratorio.id = :laboratorioId AND " +
           "a.estado IN (com.gestion_laboratorios.asignacion_lab.entity.Asignacion.EstadoAsignacion.PROGRAMADA, " +
           "com.gestion_laboratorios.asignacion_lab.entity.Asignacion.EstadoAsignacion.EN_PROGRESO)")
    long countAsignacionesActivas(@Param("laboratorioId") Long laboratorioId);
}