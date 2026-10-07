package com.miguel.tallerapi.dto.orden;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record OrdenTrabajoRequest(
        @NotNull(message = "El id del vehiculo es obligatorio")
        Long vehiculoId,

        @NotBlank(message = "La descripcion del problema es obligatoria")
        String descripcionProblema,

        @PositiveOrZero(message = "El coste estimado no puede ser negativo")
        BigDecimal costeEstimado
) {
}
