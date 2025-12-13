package com.gestion_laboratorios.asignacion_lab.dto;

import com.gestion_laboratorios.asignacion_lab.entity.Asignacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionResponseDto {

    private Long id;
    private Long usuarioId;
    private String nombrePaciente;
    private LaboratorioResponseDto laboratorio;
    private TipoAnalisisResponseDto tipoAnalisis;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Asignacion.EstadoAsignacion estado;
    private String observaciones;
    private String resultados;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public static AsignacionResponseDto fromEntity(Asignacion asignacion) {
        return AsignacionResponseDto.builder()
                .id(asignacion.getId())
                .usuarioId(asignacion.getUsuarioId())
                .nombrePaciente(asignacion.getNombrePaciente())
                .laboratorio(asignacion.getLaboratorio() != null ? 
                    LaboratorioResponseDto.fromEntity(asignacion.getLaboratorio()) : null)
                .tipoAnalisis(asignacion.getTipoAnalisis() != null ? 
                    TipoAnalisisResponseDto.fromEntity(asignacion.getTipoAnalisis()) : null)
                .fechaHoraInicio(asignacion.getFechaHoraInicio())
                .fechaHoraFin(asignacion.getFechaHoraFin())
                .estado(asignacion.getEstado())
                .observaciones(asignacion.getObservaciones())
                .resultados(asignacion.getResultados())
                .fechaCreacion(asignacion.getFechaCreacion())
                .fechaActualizacion(asignacion.getFechaActualizacion())
                .build();
    }
}