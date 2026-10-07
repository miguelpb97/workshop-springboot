package com.miguel.tallerapi.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClienteRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{8}[A-Za-z]", message = "El DNI debe tener 8 numeros y una letra")
        String dni,

        @NotBlank(message = "El telefono es obligatorio")
        String telefono,

        @Email(message = "El email no tiene un formato valido")
        String email
) {
}
