package com.miguel.tallerapi.service;

import com.miguel.tallerapi.dto.auth.AuthResponse;
import com.miguel.tallerapi.dto.auth.LoginRequest;
import com.miguel.tallerapi.dto.auth.RegisterRequest;
import com.miguel.tallerapi.entity.Usuario;
import com.miguel.tallerapi.exception.BadRequestException;
import com.miguel.tallerapi.repository.UsuarioRepository;
import com.miguel.tallerapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Ya existe un usuario con ese email");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .rol(request.rol())
                .activo(true)
                .build();

        usuarioRepository.save(usuario);

        return generarRespuestaConTokens(usuario);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("Credenciales invalidas"));

        return generarRespuestaConTokens(usuario);
    }

    private AuthResponse generarRespuestaConTokens(Usuario usuario) {
        String accessToken = jwtService.generarAccessToken(usuario);
        String refreshToken = jwtService.generarRefreshToken(usuario);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .nombre(usuario.getNombre())
                .rol(usuario.getRol().name())
                .build();
    }
}
