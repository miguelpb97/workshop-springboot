package com.miguel.tallerapi.service.impl;

import com.miguel.tallerapi.dto.orden.CambioEstadoRequest;
import com.miguel.tallerapi.dto.orden.OrdenTrabajoRequest;
import com.miguel.tallerapi.dto.orden.OrdenTrabajoResponse;
import com.miguel.tallerapi.entity.EstadoOrden;
import com.miguel.tallerapi.entity.OrdenTrabajo;
import com.miguel.tallerapi.entity.Vehiculo;
import com.miguel.tallerapi.exception.BadRequestException;
import com.miguel.tallerapi.exception.ResourceNotFoundException;
import com.miguel.tallerapi.mapper.OrdenTrabajoMapper;
import com.miguel.tallerapi.repository.OrdenTrabajoRepository;
import com.miguel.tallerapi.repository.VehiculoRepository;
import com.miguel.tallerapi.service.OrdenTrabajoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenTrabajoServiceImpl implements OrdenTrabajoService {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final OrdenTrabajoMapper ordenTrabajoMapper;

    private static final Map<EstadoOrden, List<EstadoOrden>> TRANSICIONES_VALIDAS = Map.of(
            EstadoOrden.RECIBIDA, List.of(EstadoOrden.EN_DIAGNOSTICO),
            EstadoOrden.EN_DIAGNOSTICO, List.of(EstadoOrden.EN_REPARACION, EstadoOrden.ESPERANDO_PIEZAS),
            EstadoOrden.ESPERANDO_PIEZAS, List.of(EstadoOrden.EN_REPARACION),
            EstadoOrden.EN_REPARACION, List.of(EstadoOrden.FINALIZADA),
            EstadoOrden.FINALIZADA, List.of(EstadoOrden.ENTREGADA),
            EstadoOrden.ENTREGADA, List.of()
    );

    @Override
    public OrdenTrabajoResponse crear(OrdenTrabajoRequest request) {
        Vehiculo vehiculo = vehiculoRepository.findById(request.vehiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo no encontrado con id " + request.vehiculoId()));

        OrdenTrabajo orden = ordenTrabajoMapper.toEntity(request, vehiculo);
        return ordenTrabajoMapper.toResponse(ordenTrabajoRepository.save(orden));
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenTrabajoResponse obtenerPorId(Long id) {
        return ordenTrabajoMapper.toResponse(buscarOFallar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrdenTrabajoResponse> listarPorVehiculo(Long vehiculoId, Pageable pageable) {
        Page<OrdenTrabajo> pagina = (vehiculoId == null)
                ? ordenTrabajoRepository.findAll(pageable)
                : ordenTrabajoRepository.findByVehiculoId(vehiculoId, pageable);

        return pagina.map(ordenTrabajoMapper::toResponse);
    }

    @Override
    public OrdenTrabajoResponse cambiarEstado(Long id, CambioEstadoRequest request) {
        OrdenTrabajo orden = buscarOFallar(id);
        EstadoOrden estadoActual = orden.getEstado();
        EstadoOrden nuevoEstado = request.nuevoEstado();

        List<EstadoOrden> siguientesPermitidos = TRANSICIONES_VALIDAS.get(estadoActual);
        if (!siguientesPermitidos.contains(nuevoEstado)) {
            throw new BadRequestException(
                    "No se puede pasar de %s a %s".formatted(estadoActual, nuevoEstado)
            );
        }

        orden.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoOrden.FINALIZADA) {
            orden.setCosteFinal(request.costeFinal() != null ? request.costeFinal() : orden.getCosteEstimado());
        }
        if (nuevoEstado == EstadoOrden.ENTREGADA) {
            orden.setFechaSalida(LocalDateTime.now());
        }

        return ordenTrabajoMapper.toResponse(ordenTrabajoRepository.save(orden));
    }

    private OrdenTrabajo buscarOFallar(Long id) {
        return ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de trabajo no encontrada con id " + id));
    }
}
