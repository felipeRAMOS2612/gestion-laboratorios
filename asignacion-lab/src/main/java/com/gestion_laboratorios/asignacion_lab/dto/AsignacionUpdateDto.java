package com.gestion_laboratorios.asignacion_lab.dto;

import com.gestion_laboratorios.asignacion_lab.entity.Asignacion;
import jakarta.validation.constraints.Future;
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
public class AsignacionUpdateDto {

    @Size(max = 150, message = "El nombre del paciente no puede exceder 150 caracteres")
    private String nombrePaciente;

    private Long laboratorioId;

    private Long tipoAnalisisId;

    @Future(message = "La fecha y hora de inicio debe ser futura")
    private LocalDateTime fechaHoraInicio;

    private Asignacion.EstadoAsignacion estado;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;

    @Size(max = 1000, message = "Los resultados no pueden exceder 1000 caracteres")
    private String resultados;
}