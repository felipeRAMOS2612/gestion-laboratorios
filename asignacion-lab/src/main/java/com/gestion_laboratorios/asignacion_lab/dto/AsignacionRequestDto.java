package com.gestion_laboratorios.asignacion_lab.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionRequestDto {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @Size(max = 150, message = "El nombre del paciente no puede exceder 150 caracteres")
    private String nombrePaciente;

    @NotNull(message = "El ID del laboratorio es obligatorio")
    private Long laboratorioId;

    @NotNull(message = "El ID del tipo de análisis es obligatorio")
    private Long tipoAnalisisId;

    @NotNull(message = "La fecha y hora de inicio son obligatorias")
    @Future(message = "La fecha y hora de inicio debe ser futura")
    private LocalDateTime fechaHoraInicio;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
}