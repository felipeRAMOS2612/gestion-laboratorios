package com.gestion_laboratorios.asignacion_lab.controller;

import com.gestion_laboratorios.asignacion_lab.dto.TipoAnalisisRequestDto;
import com.gestion_laboratorios.asignacion_lab.dto.TipoAnalisisResponseDto;
import com.gestion_laboratorios.asignacion_lab.service.TipoAnalisisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-analisis")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TipoAnalisisController {

    private final TipoAnalisisService tipoAnalisisService;

    @PostMapping
    public ResponseEntity<TipoAnalisisResponseDto> crearTipoAnalisis(@Valid @RequestBody TipoAnalisisRequestDto request) {
        TipoAnalisisResponseDto tipoAnalisis = tipoAnalisisService.crearTipoAnalisis(request);
        return new ResponseEntity<>(tipoAnalisis, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TipoAnalisisResponseDto>> obtenerTodosLosTiposAnalisis() {
        List<TipoAnalisisResponseDto> tipos = tipoAnalisisService.obtenerTodosLosTiposAnalisis();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<TipoAnalisisResponseDto>> obtenerTiposAnalisisActivos() {
        List<TipoAnalisisResponseDto> tipos = tipoAnalisisService.obtenerTiposAnalisisActivos();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoAnalisisResponseDto> obtenerTipoAnalisisPorId(@PathVariable Long id) {
        TipoAnalisisResponseDto tipoAnalisis = tipoAnalisisService.obtenerTipoAnalisisPorId(id);
        return ResponseEntity.ok(tipoAnalisis);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoAnalisisResponseDto> actualizarTipoAnalisis(
            @PathVariable Long id, 
            @Valid @RequestBody TipoAnalisisRequestDto request) {
        TipoAnalisisResponseDto tipoAnalisis = tipoAnalisisService.actualizarTipoAnalisis(id, request);
        return ResponseEntity.ok(tipoAnalisis);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTipoAnalisis(@PathVariable Long id) {
        tipoAnalisisService.eliminarTipoAnalisis(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TipoAnalisisResponseDto> activarDesactivar(
            @PathVariable Long id, 
            @RequestParam boolean activo) {
        TipoAnalisisResponseDto tipoAnalisis = tipoAnalisisService.activarDesactivarTipoAnalisis(id, activo);
        return ResponseEntity.ok(tipoAnalisis);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<TipoAnalisisResponseDto>> buscarTiposAnalisis(@RequestParam String q) {
        List<TipoAnalisisResponseDto> tipos = tipoAnalisisService.buscarTiposAnalisis(q);
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/por-costo")
    public ResponseEntity<List<TipoAnalisisResponseDto>> obtenerPorRangoCosto(
            @RequestParam BigDecimal costoMin,
            @RequestParam BigDecimal costoMax) {
        List<TipoAnalisisResponseDto> tipos = tipoAnalisisService.obtenerPorRangoCosto(costoMin, costoMax);
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Controlador de tipos de análisis funcionando correctamente");
    }
}