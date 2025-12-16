package com.gestion_laboratorios.asignacion_lab.service;

import com.gestion_laboratorios.asignacion_lab.dto.LaboratorioResponseDto;
import com.gestion_laboratorios.asignacion_lab.entity.Laboratorio;
import com.gestion_laboratorios.asignacion_lab.entity.MedicoLaboratorio;
import com.gestion_laboratorios.asignacion_lab.exception.ConflictException;
import com.gestion_laboratorios.asignacion_lab.exception.ResourceNotFoundException;
import com.gestion_laboratorios.asignacion_lab.repository.LaboratorioRepository;
import com.gestion_laboratorios.asignacion_lab.repository.MedicoLaboratorioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MedicoLaboratorioService {

    private final MedicoLaboratorioRepository medicoLaboratorioRepository;
    private final LaboratorioRepository laboratorioRepository;

    public void asignarLaboratorioAMedico(Long medicoId, Long laboratorioId) {
        log.info("Asignando laboratorio {} al médico {}", laboratorioId, medicoId);

        if (medicoLaboratorioRepository.existsByMedicoIdAndLaboratorio_Id(medicoId, laboratorioId)) {
            throw ConflictException.duplicateResource("Asignación médico-laboratorio (medicoId=" + medicoId + ", laboratorioId=" + laboratorioId + ")");
        }

        Laboratorio laboratorio = laboratorioRepository.findById(laboratorioId)
                .orElseThrow(() -> ResourceNotFoundException.laboratorio(laboratorioId));

        MedicoLaboratorio asignacion = MedicoLaboratorio.builder()
                .medicoId(medicoId)
                .laboratorio(laboratorio)
                .build();

        medicoLaboratorioRepository.save(asignacion);
    }

    public void desasignarLaboratorioDeMedico(Long medicoId, Long laboratorioId) {
        log.info("Desasignando laboratorio {} del médico {}", laboratorioId, medicoId);
        medicoLaboratorioRepository.deleteByMedicoIdAndLaboratorio_Id(medicoId, laboratorioId);
    }

    @Transactional(readOnly = true)
    public List<LaboratorioResponseDto> obtenerLaboratoriosDeMedico(Long medicoId) {
        return medicoLaboratorioRepository.findByMedicoIdWithLaboratorio(medicoId)
                .stream()
                .map(MedicoLaboratorio::getLaboratorio)
                .map(LaboratorioResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Long> obtenerMedicosDeLaboratorio(Long laboratorioId) {
        return medicoLaboratorioRepository.findByLaboratorio_Id(laboratorioId)
                .stream()
                .map(MedicoLaboratorio::getMedicoId)
                .distinct()
                .toList();
    }
}
