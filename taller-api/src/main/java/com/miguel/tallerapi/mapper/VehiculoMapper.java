package com.miguel.tallerapi.mapper;

import com.miguel.tallerapi.dto.vehiculo.VehiculoRequest;
import com.miguel.tallerapi.dto.vehiculo.VehiculoResponse;
import com.miguel.tallerapi.entity.Cliente;
import com.miguel.tallerapi.entity.Vehiculo;
import org.springframework.stereotype.Component;

@Component
public class VehiculoMapper {

    public Vehiculo toEntity(VehiculoRequest request, Cliente cliente) {
        return Vehiculo.builder()
                .matricula(request.matricula())
                .marca(request.marca())
                .modelo(request.modelo())
                .anio(request.anio())
                .vin(request.vin())
                .cliente(cliente)
                .build();
    }

    public VehiculoResponse toResponse(Vehiculo vehiculo) {
        return VehiculoResponse.builder()
                .id(vehiculo.getId())
                .matricula(vehiculo.getMatricula())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .anio(vehiculo.getAnio())
                .vin(vehiculo.getVin())
                .clienteId(vehiculo.getCliente().getId())
                .nombreCliente(vehiculo.getCliente().getNombre())
                .build();
    }

    public void actualizarEntidad(Vehiculo vehiculo, VehiculoRequest request) {
        vehiculo.setMatricula(request.matricula());
        vehiculo.setMarca(request.marca());
        vehiculo.setModelo(request.modelo());
        vehiculo.setAnio(request.anio());
        vehiculo.setVin(request.vin());
    }
}
