package com.miguel.tallerapi.mapper;

import com.miguel.tallerapi.dto.orden.OrdenTrabajoRequest;
import com.miguel.tallerapi.dto.orden.OrdenTrabajoResponse;
import com.miguel.tallerapi.entity.OrdenTrabajo;
import com.miguel.tallerapi.entity.Vehiculo;
import org.springframework.stereotype.Component;

@Component
public class OrdenTrabajoMapper {

    public OrdenTrabajo toEntity(OrdenTrabajoRequest request, Vehiculo vehiculo) {
        return OrdenTrabajo.builder()
                .vehiculo(vehiculo)
                .descripcionProblema(request.descripcionProblema())
                .costeEstimado(request.costeEstimado())
                .build();
    }

    public OrdenTrabajoResponse toResponse(OrdenTrabajo orden) {
        return OrdenTrabajoResponse.builder()
                .id(orden.getId())
                .vehiculoId(orden.getVehiculo().getId())
                .matriculaVehiculo(orden.getVehiculo().getMatricula())
                .descripcionProblema(orden.getDescripcionProblema())
                .estado(orden.getEstado())
                .costeEstimado(orden.getCosteEstimado())
                .costeFinal(orden.getCosteFinal())
                .fechaEntrada(orden.getFechaEntrada())
                .fechaSalida(orden.getFechaSalida())
                .build();
    }
}
