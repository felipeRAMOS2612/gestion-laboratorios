package com.gestion_laboratorios.asignacion_lab.dto;

import com.gestion_laboratorios.asignacion_lab.entity.TipoAnalisis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoAnalisisResponseDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer duracionEstimada;
    private BigDecimal costo;
    private String requisitos;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public static TipoAnalisisResponseDto fromEntity(TipoAnalisis tipoAnalisis) {
        return TipoAnalisisResponseDto.builder()
                .id(tipoAnalisis.getId())
                .nombre(tipoAnalisis.getNombre())
                .descripcion(tipoAnalisis.getDescripcion())
                .duracionEstimada(tipoAnalisis.getDuracionEstimada())
                .costo(tipoAnalisis.getCosto())
                .requisitos(tipoAnalisis.getRequisitos())
                .activo(tipoAnalisis.getActivo())
                .fechaCreacion(tipoAnalisis.getFechaCreacion())
                .fechaActualizacion(tipoAnalisis.getFechaActualizacion())
                .build();
    }
}