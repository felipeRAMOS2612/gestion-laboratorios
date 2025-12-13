package com.gestion_laboratorios.asignacion_lab.controller;

import com.gestion_laboratorios.asignacion_lab.dto.AsignacionRequestDto;
import com.gestion_laboratorios.asignacion_lab.dto.AsignacionResponseDto;
import com.gestion_laboratorios.asignacion_lab.dto.AsignacionUpdateDto;
import com.gestion_laboratorios.asignacion_lab.entity.Asignacion;
import com.gestion_laboratorios.asignacion_lab.service.AsignacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/asignaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AsignacionController {

    private final AsignacionService asignacionService;

    @PostMapping
    public ResponseEntity<AsignacionResponseDto> crearAsignacion(@Valid @RequestBody AsignacionRequestDto request) {
        AsignacionResponseDto asignacion = asignacionService.crearAsignacion(request);
        return new ResponseEntity<>(asignacion, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AsignacionResponseDto>> obtenerTodasLasAsignaciones() {
        List<AsignacionResponseDto> asignaciones = asignacionService.obtenerTodasLasAsignaciones();
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignacionResponseDto> obtenerAsignacionPorId(@PathVariable Long id) {
        AsignacionResponseDto asignacion = asignacionService.obtenerAsignacionPorId(id);
        return ResponseEntity.ok(asignacion);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AsignacionResponseDto>> obtenerAsignacionesPorUsuario(@PathVariable Long usuarioId) {
        List<AsignacionResponseDto> asignaciones = asignacionService.obtenerAsignacionesPorUsuario(usuarioId);
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/laboratorio/{laboratorioId}")
    public ResponseEntity<List<AsignacionResponseDto>> obtenerAsignacionesPorLaboratorio(@PathVariable Long laboratorioId) {
        List<AsignacionResponseDto> asignaciones = asignacionService.obtenerAsignacionesPorLaboratorio(laboratorioId);
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<AsignacionResponseDto>> obtenerAsignacionesPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<AsignacionResponseDto> asignaciones = asignacionService.obtenerAsignacionesPorFecha(fecha);
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<AsignacionResponseDto>> obtenerAsignacionesPorEstado(
            @PathVariable Asignacion.EstadoAsignacion estado) {
        List<AsignacionResponseDto> asignaciones = asignacionService.obtenerAsignacionesPorEstado(estado);
        return ResponseEntity.ok(asignaciones);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsignacionResponseDto> actualizarAsignacion(
            @PathVariable Long id, 
            @Valid @RequestBody AsignacionUpdateDto updateDto) {
        AsignacionResponseDto asignacion = asignacionService.actualizarAsignacion(id, updateDto);
        return ResponseEntity.ok(asignacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsignacion(@PathVariable Long id) {
        asignacionService.eliminarAsignacion(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<AsignacionResponseDto> iniciarAnalisis(@PathVariable Long id) {
        AsignacionResponseDto asignacion = asignacionService.iniciarAnalisis(id);
        return ResponseEntity.ok(asignacion);
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<AsignacionResponseDto> completarAnalisis(
            @PathVariable Long id,
            @RequestBody(required = false) String resultados) {
        AsignacionResponseDto asignacion = asignacionService.completarAnalisis(id, resultados);
        return ResponseEntity.ok(asignacion);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AsignacionResponseDto> cancelarAsignacion(@PathVariable Long id) {
        AsignacionResponseDto asignacion = asignacionService.cancelarAsignacion(id);
        return ResponseEntity.ok(asignacion);
    }

    @GetMapping("/buscar/paciente")
    public ResponseEntity<List<AsignacionResponseDto>> buscarPorPaciente(@RequestParam String nombre) {
        List<AsignacionResponseDto> asignaciones = asignacionService.buscarPorPaciente(nombre);
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Controlador de asignaciones funcionando correctamente");
    }
}