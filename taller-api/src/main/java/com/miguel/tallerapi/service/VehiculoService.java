package com.miguel.tallerapi.service;

import com.miguel.tallerapi.dto.vehiculo.VehiculoRequest;
import com.miguel.tallerapi.dto.vehiculo.VehiculoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehiculoService {
    VehiculoResponse crear(VehiculoRequest request);
    VehiculoResponse obtenerPorId(Long id);
    Page<VehiculoResponse> listarPorCliente(Long clienteId, Pageable pageable);
    VehiculoResponse actualizar(Long id, VehiculoRequest request);
    void eliminar(Long id);
}
