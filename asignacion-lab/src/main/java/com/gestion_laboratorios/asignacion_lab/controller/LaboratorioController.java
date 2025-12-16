package com.gestion_laboratorios.asignacion_lab.controller;

import com.gestion_laboratorios.asignacion_lab.dto.LaboratorioRequestDto;
import com.gestion_laboratorios.asignacion_lab.dto.LaboratorioResponseDto;
import com.gestion_laboratorios.asignacion_lab.entity.Laboratorio;
import com.gestion_laboratorios.asignacion_lab.service.LaboratorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratorios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LaboratorioController {

    private final LaboratorioService laboratorioService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LaboratorioResponseDto> crearLaboratorio(@Valid @RequestBody LaboratorioRequestDto request) {
        LaboratorioResponseDto laboratorio = laboratorioService.crearLaboratorio(request);
        return new ResponseEntity<>(laboratorio, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LaboratorioResponseDto>> obtenerTodosLosLaboratorios() {
        List<LaboratorioResponseDto> laboratorios = laboratorioService.obtenerTodosLosLaboratorios();
        return ResponseEntity.ok(laboratorios);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<LaboratorioResponseDto>> obtenerLaboratoriosDisponibles() {
        List<LaboratorioResponseDto> laboratorios = laboratorioService.obtenerLaboratoriosDisponibles();
        return ResponseEntity.ok(laboratorios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratorioResponseDto> obtenerLaboratorioPorId(@PathVariable Long id) {
        LaboratorioResponseDto laboratorio = laboratorioService.obtenerLaboratorioPorId(id);
        return ResponseEntity.ok(laboratorio);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LaboratorioResponseDto> actualizarLaboratorio(
            @PathVariable Long id, 
            @Valid @RequestBody LaboratorioRequestDto request) {
        LaboratorioResponseDto laboratorio = laboratorioService.actualizarLaboratorio(id, request);
        return ResponseEntity.ok(laboratorio);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarLaboratorio(@PathVariable Long id) {
        laboratorioService.eliminarLaboratorio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<LaboratorioResponseDto>> buscarLaboratorios(@RequestParam String q) {
        List<LaboratorioResponseDto> laboratorios = laboratorioService.buscarLaboratorios(q);
        return ResponseEntity.ok(laboratorios);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LaboratorioResponseDto> cambiarEstado(
            @PathVariable Long id, 
            @RequestParam Laboratorio.EstadoLaboratorio estado) {
        LaboratorioResponseDto laboratorio = laboratorioService.cambiarEstado(id, estado);
        return ResponseEntity.ok(laboratorio);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Controlador de laboratorios funcionando correctamente");
    }
}