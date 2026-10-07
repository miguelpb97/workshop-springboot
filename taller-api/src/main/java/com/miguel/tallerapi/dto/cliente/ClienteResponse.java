package com.miguel.tallerapi.dto.cliente;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ClienteResponse(
        Long id,
        String nombre,
        String dni,
        String telefono,
        String email,
        LocalDateTime fechaAlta,
        int numeroVehiculos
) {
}
