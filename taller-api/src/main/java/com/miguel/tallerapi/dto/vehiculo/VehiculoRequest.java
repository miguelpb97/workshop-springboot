package com.miguel.tallerapi.dto.vehiculo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehiculoRequest(
        @NotBlank(message = "La matricula es obligatoria")
        String matricula,

        @NotBlank(message = "La marca es obligatoria")
        String marca,

        @NotBlank(message = "El modelo es obligatorio")
        String modelo,

        @Min(value = 1950, message = "El año no es valido")
        @Max(value = 2100, message = "El año no es valido")
        Integer anio,

        String vin,

        @NotNull(message = "El id del cliente es obligatorio")
        Long clienteId
) {
}
