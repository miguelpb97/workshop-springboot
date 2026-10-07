package com.miguel.tallerapi.service;

import com.miguel.tallerapi.dto.cliente.ClienteRequest;
import com.miguel.tallerapi.dto.cliente.ClienteResponse;
import com.miguel.tallerapi.entity.Cliente;
import com.miguel.tallerapi.exception.BadRequestException;
import com.miguel.tallerapi.exception.ResourceNotFoundException;
import com.miguel.tallerapi.mapper.ClienteMapper;
import com.miguel.tallerapi.repository.ClienteRepository;
import com.miguel.tallerapi.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private ClienteRequest request;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        request = new ClienteRequest("Juan Perez", "12345678A", "600123456", "juan@example.com");
        cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan Perez")
                .dni("12345678A")
                .telefono("600123456")
                .email("juan@example.com")
                .build();
    }

    @Test
    void crear_deberiaGuardarClienteCuandoElDniNoExiste() {
        when(clienteRepository.existsByDni("12345678A")).thenReturn(false);
        when(clienteMapper.toEntity(request)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toResponse(cliente)).thenReturn(
                ClienteResponse.builder().id(1L).nombre("Juan Perez").dni("12345678A").build()
        );

        ClienteResponse resultado = clienteService.crear(request);

        assertThat(resultado.nombre()).isEqualTo("Juan Perez");
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void crear_deberiaLanzarExcepcionSiElDniYaExiste() {
        when(clienteRepository.existsByDni("12345678A")).thenReturn(true);

        assertThatThrownBy(() -> clienteService.crear(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("12345678A");

        verify(clienteRepository, never()).save(any());
    }

    @Test
    void obtenerPorId_deberiaLanzarExcepcionSiNoExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.obtenerPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
