package com.gestion_laboratorios.asignacion_lab.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tipos_analisis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoAnalisis {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_analisis_seq")
    @SequenceGenerator(name = "tipo_analisis_seq", sequenceName = "tipo_analisis_sequence", allocationSize = 1)
    private Long id;

    @NotBlank(message = "El nombre del tipo de análisis es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(length = 500)
    private String descripcion;

    @Column(name = "duracion_estimada")
    private Integer duracionEstimada; // en minutos

    @Column(precision = 10, scale = 2)
    private BigDecimal costo;

    @Size(max = 200, message = "Los requisitos no pueden exceder 200 caracteres")
    @Column(length = 200)
    private String requisitos;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "tipoAnalisis", fetch = FetchType.LAZY)
    private List<Asignacion> asignaciones;

    // Métodos helper
    public boolean estaActivo() {
        return Boolean.TRUE.equals(this.activo);
    }
}