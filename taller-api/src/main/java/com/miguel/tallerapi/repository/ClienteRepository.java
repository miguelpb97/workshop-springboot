package com.miguel.tallerapi.repository;

import com.miguel.tallerapi.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDni(String dni);
    boolean existsByDni(String dni);
    Page<Cliente> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
