package com.miguel.tallerapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes_trabajo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    @JsonIgnore
    private Vehiculo vehiculo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcionProblema;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;

    private BigDecimal costeEstimado;

    private BigDecimal costeFinal;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaEntrada;

    private LocalDateTime fechaSalida;

    @PrePersist
    public void prePersist() {
        this.fechaEntrada = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoOrden.RECIBIDA;
        }
    }
}
