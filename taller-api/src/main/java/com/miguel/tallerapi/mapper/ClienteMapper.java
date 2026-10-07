package com.miguel.tallerapi.mapper;

import com.miguel.tallerapi.dto.cliente.ClienteRequest;
import com.miguel.tallerapi.dto.cliente.ClienteResponse;
import com.miguel.tallerapi.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequest request) {
        return Cliente.builder()
                .nombre(request.nombre())
                .dni(request.dni())
                .telefono(request.telefono())
                .email(request.email())
                .build();
    }

    public ClienteResponse toResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .dni(cliente.getDni())
                .telefono(cliente.getTelefono())
                .email(cliente.getEmail())
                .fechaAlta(cliente.getFechaAlta())
                .numeroVehiculos(cliente.getVehiculos() != null ? cliente.getVehiculos().size() : 0)
                .build();
    }

    public void actualizarEntidad(Cliente cliente, ClienteRequest request) {
        cliente.setNombre(request.nombre());
        cliente.setDni(request.dni());
        cliente.setTelefono(request.telefono());
        cliente.setEmail(request.email());
    }
}
