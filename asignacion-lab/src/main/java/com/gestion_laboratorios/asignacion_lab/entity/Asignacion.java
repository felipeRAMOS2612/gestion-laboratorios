package com.gestion_laboratorios.asignacion_lab.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "asignaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asignacion_seq")
    @SequenceGenerator(name = "asignacion_seq", sequenceName = "asignacion_sequence", allocationSize = 1)
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Size(max = 150, message = "El nombre del paciente no puede exceder 150 caracteres")
    @Column(name = "nombre_paciente", length = 150)
    private String nombrePaciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratorio_id", nullable = false)
    private Laboratorio laboratorio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_analisis_id", nullable = false)
    private TipoAnalisis tipoAnalisis;

    @NotNull(message = "La fecha y hora de inicio son obligatorias")
    @Future(message = "La fecha y hora de inicio debe ser futura")
    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private EstadoAsignacion estado = EstadoAsignacion.PROGRAMADA;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(length = 500)
    private String observaciones;

    @Size(max = 1000, message = "Los resultados no pueden exceder 1000 caracteres")
    @Column(length = 1000)
    private String resultados;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public enum EstadoAsignacion {
        PROGRAMADA,
        EN_PROGRESO,
        COMPLETADA,
        CANCELADA,
        REPROGRAMADA
    }

    // Métodos helper
    public boolean estaProgramada() {
        return EstadoAsignacion.PROGRAMADA.equals(this.estado);
    }

    public boolean estaEnProgreso() {
        return EstadoAsignacion.EN_PROGRESO.equals(this.estado);
    }

    public boolean estaCompletada() {
        return EstadoAsignacion.COMPLETADA.equals(this.estado);
    }

    public boolean estaCancelada() {
        return EstadoAsignacion.CANCELADA.equals(this.estado);
    }

    public void iniciarAnalisis() {
        this.estado = EstadoAsignacion.EN_PROGRESO;
        this.fechaHoraInicio = LocalDateTime.now();
    }

    public void completarAnalisis() {
        this.estado = EstadoAsignacion.COMPLETADA;
        this.fechaHoraFin = LocalDateTime.now();
    }

    public void cancelarAsignacion() {
        this.estado = EstadoAsignacion.CANCELADA;
    }
}