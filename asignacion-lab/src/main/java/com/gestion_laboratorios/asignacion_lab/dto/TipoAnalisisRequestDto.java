package com.gestion_laboratorios.asignacion_lab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoAnalisisRequestDto {

    @NotBlank(message = "El nombre del tipo de análisis es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Positive(message = "La duración estimada debe ser un número positivo")
    private Integer duracionEstimada;

    @Positive(message = "El costo debe ser un número positivo")
    private BigDecimal costo;

    @Size(max = 200, message = "Los requisitos no pueden exceder 200 caracteres")
    private String requisitos;
}