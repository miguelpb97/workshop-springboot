package com.miguel.tallerapi.service.impl;

import com.miguel.tallerapi.dto.vehiculo.VehiculoRequest;
import com.miguel.tallerapi.dto.vehiculo.VehiculoResponse;
import com.miguel.tallerapi.entity.Cliente;
import com.miguel.tallerapi.entity.Vehiculo;
import com.miguel.tallerapi.exception.BadRequestException;
import com.miguel.tallerapi.exception.ResourceNotFoundException;
import com.miguel.tallerapi.mapper.VehiculoMapper;
import com.miguel.tallerapi.repository.ClienteRepository;
import com.miguel.tallerapi.repository.VehiculoRepository;
import com.miguel.tallerapi.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoMapper vehiculoMapper;

    @Override
    public VehiculoResponse crear(VehiculoRequest request) {
        if (vehiculoRepository.existsByMatricula(request.matricula())) {
            throw new BadRequestException("Ya existe un vehiculo con la matricula " + request.matricula());
        }

        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + request.clienteId()));

        Vehiculo vehiculo = vehiculoMapper.toEntity(request, cliente);
        return vehiculoMapper.toResponse(vehiculoRepository.save(vehiculo));
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculoResponse obtenerPorId(Long id) {
        return vehiculoMapper.toResponse(buscarOFallar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehiculoResponse> listarPorCliente(Long clienteId, Pageable pageable) {
        Page<Vehiculo> pagina = (clienteId == null)
                ? vehiculoRepository.findAll(pageable)
                : vehiculoRepository.findByClienteId(clienteId, pageable);

        return pagina.map(vehiculoMapper::toResponse);
    }

    @Override
    public VehiculoResponse actualizar(Long id, VehiculoRequest request) {
        Vehiculo vehiculo = buscarOFallar(id);
        vehiculoMapper.actualizarEntidad(vehiculo, request);
        return vehiculoMapper.toResponse(vehiculoRepository.save(vehiculo));
    }

    @Override
    public void eliminar(Long id) {
        vehiculoRepository.delete(buscarOFallar(id));
    }

    private Vehiculo buscarOFallar(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo no encontrado con id " + id));
    }
}
