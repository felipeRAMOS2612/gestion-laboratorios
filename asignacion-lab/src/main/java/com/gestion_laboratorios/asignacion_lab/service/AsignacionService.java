package com.gestion_laboratorios.asignacion_lab.service;

import com.gestion_laboratorios.asignacion_lab.dto.AsignacionRequestDto;
import com.gestion_laboratorios.asignacion_lab.dto.AsignacionResponseDto;
import com.gestion_laboratorios.asignacion_lab.dto.AsignacionUpdateDto;
import com.gestion_laboratorios.asignacion_lab.entity.Asignacion;
import com.gestion_laboratorios.asignacion_lab.entity.Laboratorio;
import com.gestion_laboratorios.asignacion_lab.entity.TipoAnalisis;
import com.gestion_laboratorios.asignacion_lab.exception.BusinessException;
import com.gestion_laboratorios.asignacion_lab.exception.ConflictException;
import com.gestion_laboratorios.asignacion_lab.exception.ResourceNotFoundException;
import com.gestion_laboratorios.asignacion_lab.repository.AsignacionRepository;
import com.gestion_laboratorios.asignacion_lab.repository.LaboratorioRepository;
import com.gestion_laboratorios.asignacion_lab.repository.TipoAnalisisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final TipoAnalisisRepository tipoAnalisisRepository;

    public AsignacionResponseDto crearAsignacion(AsignacionRequestDto request) {
        log.info("Creando asignación para usuario: {}", request.getUsuarioId());
        
        // Validar que el laboratorio existe y está disponible
        Laboratorio laboratorio = laboratorioRepository.findById(request.getLaboratorioId())
                .orElseThrow(() -> ResourceNotFoundException.laboratorio(request.getLaboratorioId()));
        
        if (!laboratorio.estaDisponible()) {
            throw ConflictException.laboratorioEnMantenimiento();
        }
        
        // Validar que el tipo de análisis existe y está activo
        TipoAnalisis tipoAnalisis = tipoAnalisisRepository.findById(request.getTipoAnalisisId())
                .orElseThrow(() -> ResourceNotFoundException.tipoAnalisis(request.getTipoAnalisisId()));
        
        if (!tipoAnalisis.estaActivo()) {
            throw BusinessException.tipoAnalisisInactivo();
        }
        
        // Validar fecha futura
        if (request.getFechaHoraInicio().isBefore(LocalDateTime.now())) {
            throw BusinessException.fechaInvalida();
        }
        
        // Calcular fecha de fin estimada
        LocalDateTime fechaHoraFin = request.getFechaHoraInicio()
                .plusMinutes(tipoAnalisis.getDuracionEstimada() != null ? tipoAnalisis.getDuracionEstimada() : 60);
        
        // Verificar conflictos de horario
        List<Asignacion> conflictos = asignacionRepository.findConflictosHorario(
            request.getLaboratorioId(),
            request.getFechaHoraInicio(),
            fechaHoraFin
        );
        
        if (!conflictos.isEmpty()) {
            throw ConflictException.horarioConflicto();
        }
        
        // Verificar capacidad del laboratorio
        long asignacionesDelDia = asignacionRepository.countAsignacionesByLaboratorioAndFecha(
            request.getLaboratorioId(),
            request.getFechaHoraInicio()
        );
        
        if (laboratorio.getCapacidadMaxima() != null && asignacionesDelDia >= laboratorio.getCapacidadMaxima()) {
            throw BusinessException.capacidadExcedida();
        }

        Asignacion asignacion = Asignacion.builder()
                .usuarioId(request.getUsuarioId())
                .nombrePaciente(request.getNombrePaciente())
                .laboratorio(laboratorio)
                .tipoAnalisis(tipoAnalisis)
                .fechaHoraInicio(request.getFechaHoraInicio())
                .fechaHoraFin(fechaHoraFin)
                .observaciones(request.getObservaciones())
                .estado(Asignacion.EstadoAsignacion.PROGRAMADA)
                .build();

        Asignacion asignacionGuardada = asignacionRepository.save(asignacion);
        log.info("Asignación creada exitosamente con ID: {}", asignacionGuardada.getId());
        
        return AsignacionResponseDto.fromEntity(asignacionGuardada);
    }

    @Transactional(readOnly = true)
    public List<AsignacionResponseDto> obtenerTodasLasAsignaciones() {
        log.info("Obteniendo todas las asignaciones");
        return asignacionRepository.findAll()
                .stream()
                .map(AsignacionResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AsignacionResponseDto> obtenerAsignacionesPorUsuario(Long usuarioId) {
        log.info("Obteniendo asignaciones para usuario: {}", usuarioId);
        return asignacionRepository.findByUsuarioIdOrderByFechaHoraInicioDesc(usuarioId)
                .stream()
                .map(AsignacionResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AsignacionResponseDto> obtenerAsignacionesPorLaboratorio(Long laboratorioId) {
        log.info("Obteniendo asignaciones para laboratorio: {}", laboratorioId);
        return asignacionRepository.findByLaboratorioId(laboratorioId)
                .stream()
                .map(AsignacionResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AsignacionResponseDto obtenerAsignacionPorId(Long id) {
        log.info("Buscando asignación por ID: {}", id);
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.asignacion(id));
        
        return AsignacionResponseDto.fromEntity(asignacion);
    }

    public AsignacionResponseDto actualizarAsignacion(Long id, AsignacionUpdateDto updateDto) {
        log.info("Actualizando asignación con ID: {}", id);
        
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.asignacion(id));

        // Validar que la asignación se puede modificar
        if (asignacion.estaCompletada() || asignacion.estaCancelada()) {
            throw ConflictException.asignacionNoModificable();
        }

        // Actualizar campos si están presentes
        if (updateDto.getNombrePaciente() != null) {
            asignacion.setNombrePaciente(updateDto.getNombrePaciente());
        }
        
        if (updateDto.getLaboratorioId() != null) {
            Laboratorio laboratorio = laboratorioRepository.findById(updateDto.getLaboratorioId())
                    .orElseThrow(() -> ResourceNotFoundException.laboratorio(updateDto.getLaboratorioId()));
            asignacion.setLaboratorio(laboratorio);
        }
        
        if (updateDto.getTipoAnalisisId() != null) {
            TipoAnalisis tipoAnalisis = tipoAnalisisRepository.findById(updateDto.getTipoAnalisisId())
                    .orElseThrow(() -> ResourceNotFoundException.tipoAnalisis(updateDto.getTipoAnalisisId()));
            asignacion.setTipoAnalisis(tipoAnalisis);
        }
        
        if (updateDto.getFechaHoraInicio() != null) {
            if (updateDto.getFechaHoraInicio().isBefore(LocalDateTime.now())) {
                throw BusinessException.fechaInvalida();
            }
            asignacion.setFechaHoraInicio(updateDto.getFechaHoraInicio());
        }
        
        if (updateDto.getEstado() != null) {
            asignacion.setEstado(updateDto.getEstado());
        }
        
        if (updateDto.getObservaciones() != null) {
            asignacion.setObservaciones(updateDto.getObservaciones());
        }
        
        if (updateDto.getResultados() != null) {
            asignacion.setResultados(updateDto.getResultados());
        }

        Asignacion asignacionActualizada = asignacionRepository.save(asignacion);
        log.info("Asignación actualizada exitosamente");
        
        return AsignacionResponseDto.fromEntity(asignacionActualizada);
    }

    public void eliminarAsignacion(Long id) {
        log.info("Eliminando asignación con ID: {}", id);
        
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.asignacion(id));
        
        // Solo permitir eliminar asignaciones programadas
        if (!asignacion.estaProgramada()) {
            throw ConflictException.asignacionNoModificable();
        }
        
        asignacionRepository.delete(asignacion);
        log.info("Asignación eliminada exitosamente");
    }

    public AsignacionResponseDto iniciarAnalisis(Long id) {
        log.info("Iniciando análisis para asignación: {}", id);
        
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.asignacion(id));
        
        if (!asignacion.estaProgramada()) {
            throw ConflictException.asignacionNoModificable();
        }
        
        asignacion.iniciarAnalisis();
        Asignacion asignacionActualizada = asignacionRepository.save(asignacion);
        
        return AsignacionResponseDto.fromEntity(asignacionActualizada);
    }

    public AsignacionResponseDto completarAnalisis(Long id, String resultados) {
        log.info("Completando análisis para asignación: {}", id);
        
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.asignacion(id));
        
        if (!asignacion.estaEnProgreso()) {
            throw ConflictException.asignacionNoModificable();
        }
        
        asignacion.completarAnalisis();
        if (resultados != null) {
            asignacion.setResultados(resultados);
        }
        
        Asignacion asignacionActualizada = asignacionRepository.save(asignacion);
        
        return AsignacionResponseDto.fromEntity(asignacionActualizada);
    }

    public AsignacionResponseDto cancelarAsignacion(Long id) {
        log.info("Cancelando asignación: {}", id);
        
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.asignacion(id));
        
        if (asignacion.estaCompletada() || asignacion.estaCancelada()) {
            throw ConflictException.asignacionNoModificable();
        }
        
        asignacion.cancelarAsignacion();
        Asignacion asignacionActualizada = asignacionRepository.save(asignacion);
        
        return AsignacionResponseDto.fromEntity(asignacionActualizada);
    }

    @Transactional(readOnly = true)
    public List<AsignacionResponseDto> obtenerAsignacionesPorFecha(LocalDate fecha) {
        log.info("Obteniendo asignaciones para fecha: {}", fecha);
        LocalDateTime inicio = fecha.atTime(LocalTime.MIN);
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        
        return asignacionRepository.findByFechaHoraInicioBetween(inicio, fin)
                .stream()
                .map(AsignacionResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AsignacionResponseDto> obtenerAsignacionesPorEstado(Asignacion.EstadoAsignacion estado) {
        log.info("Obteniendo asignaciones con estado: {}", estado);
        return asignacionRepository.findByEstadoOrderByFechaHoraInicio(estado)
                .stream()
                .map(AsignacionResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AsignacionResponseDto> buscarPorPaciente(String nombrePaciente) {
        log.info("Buscando asignaciones por paciente: {}", nombrePaciente);
        return asignacionRepository.findByNombrePacienteContainingIgnoreCase(nombrePaciente)
                .stream()
                .map(AsignacionResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}