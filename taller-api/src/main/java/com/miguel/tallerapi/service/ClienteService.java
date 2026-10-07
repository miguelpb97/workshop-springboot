package com.miguel.tallerapi.service;

import com.miguel.tallerapi.dto.cliente.ClienteRequest;
import com.miguel.tallerapi.dto.cliente.ClienteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    ClienteResponse crear(ClienteRequest request);
    ClienteResponse obtenerPorId(Long id);
    Page<ClienteResponse> listar(String nombre, Pageable pageable);
    ClienteResponse actualizar(Long id, ClienteRequest request);
    void eliminar(Long id);
}
