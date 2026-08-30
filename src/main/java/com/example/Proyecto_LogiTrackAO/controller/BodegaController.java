package com.example.Proyecto_LogiTrackAO.controller;

import com.example.Proyecto_LogiTrackAO.dto.request.BodegaRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.BodegaResponse;
import com.example.Proyecto_LogiTrackAO.service.BodegaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaService bodegaService;

    @PostMapping
    public ResponseEntity<BodegaResponse> crear(@Valid @RequestBody BodegaRequest request) {
        BodegaResponse creada = bodegaService.crearBodega(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<BodegaResponse>> obtenerTodas() {
        return ResponseEntity.ok(bodegaService.obtenerBodegas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BodegaResponse> actualizar(@PathVariable Long id, @Valid @RequestBody BodegaRequest request) {
        return ResponseEntity.ok(bodegaService.actualizarBodega(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        bodegaService.eliminarBodega(id);
        return ResponseEntity.noContent().build();
    }
}