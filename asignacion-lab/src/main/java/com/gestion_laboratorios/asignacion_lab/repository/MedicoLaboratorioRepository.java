package com.gestion_laboratorios.asignacion_lab.repository;

import com.gestion_laboratorios.asignacion_lab.entity.MedicoLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicoLaboratorioRepository extends JpaRepository<MedicoLaboratorio, Long> {

    boolean existsByMedicoIdAndLaboratorio_Id(Long medicoId, Long laboratorioId);

    @Query("select ml from MedicoLaboratorio ml join fetch ml.laboratorio where ml.medicoId = :medicoId")
    List<MedicoLaboratorio> findByMedicoIdWithLaboratorio(@Param("medicoId") Long medicoId);

    List<MedicoLaboratorio> findByLaboratorio_Id(Long laboratorioId);

    @Modifying
    void deleteByMedicoIdAndLaboratorio_Id(Long medicoId, Long laboratorioId);
}
