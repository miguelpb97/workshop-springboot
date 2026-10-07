package com.miguel.tallerapi.dto.orden;

import com.miguel.tallerapi.entity.EstadoOrden;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CambioEstadoRequest(
        @NotNull(message = "El nuevo estado es obligatorio")
        EstadoOrden nuevoEstado,

        BigDecimal costeFinal
) {
}
