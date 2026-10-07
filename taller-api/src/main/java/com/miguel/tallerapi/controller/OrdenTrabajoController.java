package com.miguel.tallerapi.controller;

import com.miguel.tallerapi.dto.orden.CambioEstadoRequest;
import com.miguel.tallerapi.dto.orden.OrdenTrabajoRequest;
import com.miguel.tallerapi.dto.orden.OrdenTrabajoResponse;
import com.miguel.tallerapi.service.OrdenTrabajoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
@Tag(name = "Ordenes de trabajo", description = "Ciclo de vida de una reparacion")
public class OrdenTrabajoController {

    private final OrdenTrabajoService ordenTrabajoService;

    @PostMapping
    public ResponseEntity<OrdenTrabajoResponse> crear(@Valid @RequestBody OrdenTrabajoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenTrabajoService.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenTrabajoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ordenTrabajoService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<OrdenTrabajoResponse>> listar(
            @RequestParam(required = false) Long vehiculoId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ordenTrabajoService.listarPorVehiculo(vehiculoId, pageable));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenTrabajoResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRequest request
    ) {
        return ResponseEntity.ok(ordenTrabajoService.cambiarEstado(id, request));
    }
}
