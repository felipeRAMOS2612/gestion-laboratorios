package com.gestion_laboratorios.asignacion_lab.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "medico_laboratorio",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_medico_laboratorio_medico_laboratorio",
                        columnNames = {"medico_id", "laboratorio_id"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoLaboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medico_laboratorio_seq")
    @SequenceGenerator(name = "medico_laboratorio_seq", sequenceName = "medico_laboratorio_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "medico_id", nullable = false)
    private Long medicoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "laboratorio_id", nullable = false)
    private Laboratorio laboratorio;

    @CreationTimestamp
    @Column(name = "fecha_asignacion", updatable = false)
    private LocalDateTime fechaAsignacion;
}
