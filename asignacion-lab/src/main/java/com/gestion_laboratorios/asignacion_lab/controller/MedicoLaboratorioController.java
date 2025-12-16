package com.gestion_laboratorios.asignacion_lab.controller;

import com.gestion_laboratorios.asignacion_lab.dto.LaboratorioResponseDto;
import com.gestion_laboratorios.asignacion_lab.service.MedicoLaboratorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class MedicoLaboratorioController {

    private final MedicoLaboratorioService medicoLaboratorioService;

    @PostMapping("/medicos/{medicoId}/laboratorios/{laboratorioId}")
    public ResponseEntity<Void> asignarLaboratorioAMedico(@PathVariable Long medicoId, @PathVariable Long laboratorioId) {
        medicoLaboratorioService.asignarLaboratorioAMedico(medicoId, laboratorioId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/medicos/{medicoId}/laboratorios/{laboratorioId}")
    public ResponseEntity<Void> desasignarLaboratorioDeMedico(@PathVariable Long medicoId, @PathVariable Long laboratorioId) {
        medicoLaboratorioService.desasignarLaboratorioDeMedico(medicoId, laboratorioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medicos/{medicoId}/laboratorios")
    public ResponseEntity<List<LaboratorioResponseDto>> obtenerLaboratoriosDeMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(medicoLaboratorioService.obtenerLaboratoriosDeMedico(medicoId));
    }

    @GetMapping("/laboratorios/{laboratorioId}/medicos")
    public ResponseEntity<List<Long>> obtenerMedicosDeLaboratorio(@PathVariable Long laboratorioId) {
        return ResponseEntity.ok(medicoLaboratorioService.obtenerMedicosDeLaboratorio(laboratorioId));
    }
}
