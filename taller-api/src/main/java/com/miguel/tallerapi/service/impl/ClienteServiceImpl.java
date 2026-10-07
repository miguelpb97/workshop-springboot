package com.miguel.tallerapi.service.impl;

import com.miguel.tallerapi.dto.cliente.ClienteRequest;
import com.miguel.tallerapi.dto.cliente.ClienteResponse;
import com.miguel.tallerapi.entity.Cliente;
import com.miguel.tallerapi.exception.BadRequestException;
import com.miguel.tallerapi.exception.ResourceNotFoundException;
import com.miguel.tallerapi.mapper.ClienteMapper;
import com.miguel.tallerapi.repository.ClienteRepository;
import com.miguel.tallerapi.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public ClienteResponse crear(ClienteRequest request) {
        if (clienteRepository.existsByDni(request.dni())) {
            throw new BadRequestException("Ya existe un cliente con el DNI " + request.dni());
        }
        Cliente cliente = clienteMapper.toEntity(request);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse obtenerPorId(Long id) {
        return clienteMapper.toResponse(buscarOFallar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponse> listar(String nombre, Pageable pageable) {
        Page<Cliente> pagina = (nombre == null || nombre.isBlank())
                ? clienteRepository.findAll(pageable)
                : clienteRepository.findByNombreContainingIgnoreCase(nombre, pageable);

        return pagina.map(clienteMapper::toResponse);
    }

    @Override
    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarOFallar(id);
        clienteMapper.actualizarEntidad(cliente, request);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = buscarOFallar(id);
        clienteRepository.delete(cliente);
    }

    private Cliente buscarOFallar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
    }
}
