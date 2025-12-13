package com.gestion_laboratorios.asignacion_lab.service;

import com.gestion_laboratorios.asignacion_lab.dto.TipoAnalisisRequestDto;
import com.gestion_laboratorios.asignacion_lab.dto.TipoAnalisisResponseDto;
import com.gestion_laboratorios.asignacion_lab.entity.TipoAnalisis;
import com.gestion_laboratorios.asignacion_lab.exception.ConflictException;
import com.gestion_laboratorios.asignacion_lab.exception.ResourceNotFoundException;
import com.gestion_laboratorios.asignacion_lab.repository.TipoAnalisisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TipoAnalisisService {

    private final TipoAnalisisRepository tipoAnalisisRepository;

    public TipoAnalisisResponseDto crearTipoAnalisis(TipoAnalisisRequestDto request) {
        log.info("Creando tipo de análisis: {}", request.getNombre());
        
        if (tipoAnalisisRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw ConflictException.duplicateResource("Tipo de análisis con nombre: " + request.getNombre());
        }

        TipoAnalisis tipoAnalisis = TipoAnalisis.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .duracionEstimada(request.getDuracionEstimada())
                .costo(request.getCosto())
                .requisitos(request.getRequisitos())
                .activo(true)
                .build();

        TipoAnalisis tipoGuardado = tipoAnalisisRepository.save(tipoAnalisis);
        log.info("Tipo de análisis creado exitosamente con ID: {}", tipoGuardado.getId());
        
        return TipoAnalisisResponseDto.fromEntity(tipoGuardado);
    }

    @Transactional(readOnly = true)
    public List<TipoAnalisisResponseDto> obtenerTodosLosTiposAnalisis() {
        log.info("Obteniendo todos los tipos de análisis");
        return tipoAnalisisRepository.findAll()
                .stream()
                .map(TipoAnalisisResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TipoAnalisisResponseDto> obtenerTiposAnalisisActivos() {
        log.info("Obteniendo tipos de análisis activos");
        return tipoAnalisisRepository.findByActivoTrueOrderByNombre()
                .stream()
                .map(TipoAnalisisResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoAnalisisResponseDto obtenerTipoAnalisisPorId(Long id) {
        log.info("Buscando tipo de análisis por ID: {}", id);
        TipoAnalisis tipoAnalisis = tipoAnalisisRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.tipoAnalisis(id));
        
        return TipoAnalisisResponseDto.fromEntity(tipoAnalisis);
    }

    public TipoAnalisisResponseDto actualizarTipoAnalisis(Long id, TipoAnalisisRequestDto request) {
        log.info("Actualizando tipo de análisis con ID: {}", id);
        
        TipoAnalisis tipoAnalisis = tipoAnalisisRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.tipoAnalisis(id));

        // Validar nombre único si se está actualizando
        if (!tipoAnalisis.getNombre().equalsIgnoreCase(request.getNombre()) && 
            tipoAnalisisRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw ConflictException.duplicateResource("Tipo de análisis con nombre: " + request.getNombre());
        }

        tipoAnalisis.setNombre(request.getNombre());
        tipoAnalisis.setDescripcion(request.getDescripcion());
        tipoAnalisis.setDuracionEstimada(request.getDuracionEstimada());
        tipoAnalisis.setCosto(request.getCosto());
        tipoAnalisis.setRequisitos(request.getRequisitos());

        TipoAnalisis tipoActualizado = tipoAnalisisRepository.save(tipoAnalisis);
        log.info("Tipo de análisis actualizado exitosamente");
        
        return TipoAnalisisResponseDto.fromEntity(tipoActualizado);
    }

    public void eliminarTipoAnalisis(Long id) {
        log.info("Eliminando tipo de análisis con ID: {}", id);
        
        TipoAnalisis tipoAnalisis = tipoAnalisisRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.tipoAnalisis(id));
        
        // Verificar que no tenga asignaciones
        long asignaciones = tipoAnalisisRepository.countAsignacionesByTipoAnalisis(id);
        if (asignaciones > 0) {
            throw ConflictException.asignacionNoModificable();
        }
        
        tipoAnalisisRepository.delete(tipoAnalisis);
        log.info("Tipo de análisis eliminado exitosamente");
    }

    public TipoAnalisisResponseDto activarDesactivarTipoAnalisis(Long id, boolean activo) {
        log.info("Cambiando estado activo del tipo de análisis {} a {}", id, activo);
        
        TipoAnalisis tipoAnalisis = tipoAnalisisRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.tipoAnalisis(id));
        
        tipoAnalisis.setActivo(activo);
        TipoAnalisis tipoActualizado = tipoAnalisisRepository.save(tipoAnalisis);
        
        return TipoAnalisisResponseDto.fromEntity(tipoActualizado);
    }

    @Transactional(readOnly = true)
    public List<TipoAnalisisResponseDto> buscarTiposAnalisis(String searchTerm) {
        log.info("Buscando tipos de análisis con término: {}", searchTerm);
        return tipoAnalisisRepository.findByActivoTrueAndSearchTerm(searchTerm)
                .stream()
                .map(TipoAnalisisResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TipoAnalisisResponseDto> obtenerPorRangoCosto(BigDecimal costoMin, BigDecimal costoMax) {
        log.info("Obteniendo tipos de análisis por rango de costo: {} - {}", costoMin, costoMax);
        return tipoAnalisisRepository.findByActivoTrueAndCostoBetween(costoMin, costoMax)
                .stream()
                .map(TipoAnalisisResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}