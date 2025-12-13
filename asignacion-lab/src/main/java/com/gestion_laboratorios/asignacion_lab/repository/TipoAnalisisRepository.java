package com.gestion_laboratorios.asignacion_lab.repository;

import com.gestion_laboratorios.asignacion_lab.entity.TipoAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TipoAnalisisRepository extends JpaRepository<TipoAnalisis, Long> {

    List<TipoAnalisis> findByActivoTrue();
    
    List<TipoAnalisis> findByActivoTrueOrderByNombre();
    
    Optional<TipoAnalisis> findByNombreIgnoreCase(String nombre);
    
    boolean existsByNombreIgnoreCase(String nombre);
    
    @Query("SELECT t FROM TipoAnalisis t WHERE t.activo = true AND t.costo BETWEEN :costoMin AND :costoMax")
    List<TipoAnalisis> findByActivoTrueAndCostoBetween(
        @Param("costoMin") BigDecimal costoMin, 
        @Param("costoMax") BigDecimal costoMax
    );
    
    @Query("SELECT t FROM TipoAnalisis t WHERE t.activo = true AND t.duracionEstimada <= :duracionMaxima")
    List<TipoAnalisis> findByActivoTrueAndDuracionEstimadaLessThanEqual(
        @Param("duracionMaxima") Integer duracionMaxima
    );
    
    @Query("SELECT t FROM TipoAnalisis t WHERE t.activo = true AND " +
           "(LOWER(t.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.descripcion) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<TipoAnalisis> findByActivoTrueAndSearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(a) FROM Asignacion a WHERE a.tipoAnalisis.id = :tipoAnalisisId")
    long countAsignacionesByTipoAnalisis(@Param("tipoAnalisisId") Long tipoAnalisisId);
}