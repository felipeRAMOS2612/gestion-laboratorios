package com.gestion_laboratorios.asignacion_lab.dto;

import com.gestion_laboratorios.asignacion_lab.entity.Laboratorio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaboratorioRequestDto {

    @NotBlank(message = "El nombre del laboratorio es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 200, message = "La ubicación no puede exceder 200 caracteres")
    private String ubicacion;

    @Positive(message = "La capacidad máxima debe ser un número positivo")
    private Integer capacidadMaxima;

    @NotNull(message = "El estado es obligatorio")
    private Laboratorio.EstadoLaboratorio estado;

    @Size(max = 200, message = "El equipamiento no puede exceder 200 caracteres")
    private String equipamiento;
}