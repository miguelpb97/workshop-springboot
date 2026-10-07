package com.miguel.tallerapi.dto.vehiculo;

import lombok.Builder;

@Builder
public record VehiculoResponse(
        Long id,
        String matricula,
        String marca,
        String modelo,
        Integer anio,
        String vin,
        Long clienteId,
        String nombreCliente
) {
}
