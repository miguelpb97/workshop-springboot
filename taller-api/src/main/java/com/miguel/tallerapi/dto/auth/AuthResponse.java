package com.miguel.tallerapi.dto.auth;

import lombok.Builder;

@Builder
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        String nombre,
        String rol
) {
}
