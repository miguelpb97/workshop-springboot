package com.miguel.tallerapi.service;

import com.miguel.tallerapi.dto.orden.CambioEstadoRequest;
import com.miguel.tallerapi.dto.orden.OrdenTrabajoRequest;
import com.miguel.tallerapi.dto.orden.OrdenTrabajoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdenTrabajoService {
    OrdenTrabajoResponse crear(OrdenTrabajoRequest request);
    OrdenTrabajoResponse obtenerPorId(Long id);
    Page<OrdenTrabajoResponse> listarPorVehiculo(Long vehiculoId, Pageable pageable);
    OrdenTrabajoResponse cambiarEstado(Long id, CambioEstadoRequest request);
}
