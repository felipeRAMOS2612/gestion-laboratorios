package com.gestion_laboratorios.asignacion_lab.service;

import com.gestion_laboratorios.asignacion_lab.dto.LaboratorioRequestDto;
import com.gestion_laboratorios.asignacion_lab.dto.LaboratorioResponseDto;
import com.gestion_laboratorios.asignacion_lab.entity.Laboratorio;
import com.gestion_laboratorios.asignacion_lab.exception.ConflictException;
import com.gestion_laboratorios.asignacion_lab.exception.ResourceNotFoundException;
import com.gestion_laboratorios.asignacion_lab.repository.LaboratorioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LaboratorioService {

    private final LaboratorioRepository laboratorioRepository;

    public LaboratorioResponseDto crearLaboratorio(LaboratorioRequestDto request) {
        log.info("Creando laboratorio: {}", request.getNombre());
        
        if (laboratorioRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw ConflictException.duplicateResource("Laboratorio con nombre: " + request.getNombre());
        }

        Laboratorio laboratorio = Laboratorio.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .ubicacion(request.getUbicacion())
                .capacidadMaxima(request.getCapacidadMaxima())
                .estado(request.getEstado())
                .equipamiento(request.getEquipamiento())
                .build();

        Laboratorio laboratorioGuardado = laboratorioRepository.save(laboratorio);
        log.info("Laboratorio creado exitosamente con ID: {}", laboratorioGuardado.getId());
        
        return LaboratorioResponseDto.fromEntity(laboratorioGuardado);
    }

    @Transactional(readOnly = true)
    public List<LaboratorioResponseDto> obtenerTodosLosLaboratorios() {
        log.info("Obteniendo todos los laboratorios");
        return laboratorioRepository.findAll()
                .stream()
                .map(LaboratorioResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LaboratorioResponseDto> obtenerLaboratoriosDisponibles() {
        log.info("Obteniendo laboratorios disponibles");
        return laboratorioRepository.findByEstadoOrderByNombre(Laboratorio.EstadoLaboratorio.DISPONIBLE)
                .stream()
                .map(LaboratorioResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LaboratorioResponseDto obtenerLaboratorioPorId(Long id) {
        log.info("Buscando laboratorio por ID: {}", id);
        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.laboratorio(id));
        
        return LaboratorioResponseDto.fromEntity(laboratorio);
    }

    public LaboratorioResponseDto actualizarLaboratorio(Long id, LaboratorioRequestDto request) {
        log.info("Actualizando laboratorio con ID: {}", id);
        
        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.laboratorio(id));

        // Validar nombre único si se está actualizando
        if (!laboratorio.getNombre().equalsIgnoreCase(request.getNombre()) && 
            laboratorioRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw ConflictException.duplicateResource("Laboratorio con nombre: " + request.getNombre());
        }

        laboratorio.setNombre(request.getNombre());
        laboratorio.setDescripcion(request.getDescripcion());
        laboratorio.setUbicacion(request.getUbicacion());
        laboratorio.setCapacidadMaxima(request.getCapacidadMaxima());
        laboratorio.setEstado(request.getEstado());
        laboratorio.setEquipamiento(request.getEquipamiento());

        Laboratorio laboratorioActualizado = laboratorioRepository.save(laboratorio);
        log.info("Laboratorio actualizado exitosamente");
        
        return LaboratorioResponseDto.fromEntity(laboratorioActualizado);
    }

    public void eliminarLaboratorio(Long id) {
        log.info("Eliminando laboratorio con ID: {}", id);
        
        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.laboratorio(id));
        
        // Verificar que no tenga asignaciones activas
        long asignacionesActivas = laboratorioRepository.countAsignacionesActivas(id);
        if (asignacionesActivas > 0) {
            throw ConflictException.asignacionNoModificable();
        }
        
        laboratorioRepository.delete(laboratorio);
        log.info("Laboratorio eliminado exitosamente");
    }

    @Transactional(readOnly = true)
    public List<LaboratorioResponseDto> buscarLaboratorios(String searchTerm) {
        log.info("Buscando laboratorios con término: {}", searchTerm);
        return laboratorioRepository.findBySearchTerm(searchTerm)
                .stream()
                .map(LaboratorioResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public LaboratorioResponseDto cambiarEstado(Long id, Laboratorio.EstadoLaboratorio nuevoEstado) {
        log.info("Cambiando estado del laboratorio {} a {}", id, nuevoEstado);
        
        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.laboratorio(id));
        
        laboratorio.setEstado(nuevoEstado);
        Laboratorio laboratorioActualizado = laboratorioRepository.save(laboratorio);
        
        return LaboratorioResponseDto.fromEntity(laboratorioActualizado);
    }
}