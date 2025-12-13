package com.gestion_laboratorios.asignacion_lab.dto;

import com.gestion_laboratorios.asignacion_lab.entity.Laboratorio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaboratorioResponseDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private Integer capacidadMaxima;
    private Laboratorio.EstadoLaboratorio estado;
    private String equipamiento;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private boolean disponible;
    private int asignacionesActivas;

    public static LaboratorioResponseDto fromEntity(Laboratorio laboratorio) {
        return LaboratorioResponseDto.builder()
                .id(laboratorio.getId())
                .nombre(laboratorio.getNombre())
                .descripcion(laboratorio.getDescripcion())
                .ubicacion(laboratorio.getUbicacion())
                .capacidadMaxima(laboratorio.getCapacidadMaxima())
                .estado(laboratorio.getEstado())
                .equipamiento(laboratorio.getEquipamiento())
                .fechaCreacion(laboratorio.getFechaCreacion())
                .fechaActualizacion(laboratorio.getFechaActualizacion())
                .disponible(laboratorio.estaDisponible())
                .asignacionesActivas(laboratorio.getAsignaciones() != null ? 
                    (int) laboratorio.getAsignaciones().stream()
                        .filter(a -> !a.estaCompletada() && !a.estaCancelada())
                        .count() : 0)
                .build();
    }
}