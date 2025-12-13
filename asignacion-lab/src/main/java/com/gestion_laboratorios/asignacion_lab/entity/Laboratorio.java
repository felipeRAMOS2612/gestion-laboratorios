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

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "laboratorios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Laboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "laboratorio_seq")
    @SequenceGenerator(name = "laboratorio_seq", sequenceName = "laboratorio_sequence", allocationSize = 1)
    private Long id;

    @NotBlank(message = "El nombre del laboratorio es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(length = 500)
    private String descripcion;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 200, message = "La ubicación no puede exceder 200 caracteres")
    @Column(nullable = false, length = 200)
    private String ubicacion;

    @Column(name = "capacidad_maxima")
    private Integer capacidadMaxima;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoLaboratorio estado;

    @Size(max = 200, message = "El equipamiento no puede exceder 200 caracteres")
    @Column(length = 200)
    private String equipamiento;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "laboratorio", fetch = FetchType.LAZY)
    private List<Asignacion> asignaciones;

    public enum EstadoLaboratorio {
        DISPONIBLE,
        OCUPADO,
        MANTENIMIENTO,
        FUERA_DE_SERVICIO
    }

    // Métodos helper
    public boolean estaDisponible() {
        return EstadoLaboratorio.DISPONIBLE.equals(this.estado);
    }

    public boolean estaOcupado() {
        return EstadoLaboratorio.OCUPADO.equals(this.estado);
    }
}