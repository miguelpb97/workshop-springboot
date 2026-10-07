package com.miguel.tallerapi.repository;

import com.miguel.tallerapi.entity.EstadoOrden;
import com.miguel.tallerapi.entity.OrdenTrabajo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long> {
    Page<OrdenTrabajo> findByVehiculoId(Long vehiculoId, Pageable pageable);
    Page<OrdenTrabajo> findByEstado(EstadoOrden estado, Pageable pageable);
}
