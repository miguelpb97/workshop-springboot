package com.miguel.tallerapi.dto.orden;

import com.miguel.tallerapi.entity.EstadoOrden;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record OrdenTrabajoResponse(
        Long id,
        Long vehiculoId,
        String matriculaVehiculo,
        String descripcionProblema,
        EstadoOrden estado,
        BigDecimal costeEstimado,
        BigDecimal costeFinal,
        LocalDateTime fechaEntrada,
        LocalDateTime fechaSalida
) {
}
