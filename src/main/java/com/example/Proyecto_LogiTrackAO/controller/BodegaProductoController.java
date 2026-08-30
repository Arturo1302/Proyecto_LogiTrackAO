package com.example.Proyecto_LogiTrackAO.controller;

import com.example.Proyecto_LogiTrackAO.dto.request.BodegaProductoRequest;
import com.example.Proyecto_LogiTrackAO.dto.request.StockUpdateRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.BodegaProductoResponse;
import com.example.Proyecto_LogiTrackAO.service.BodegaProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bodega-producto")
@RequiredArgsConstructor
public class BodegaProductoController {

    private final BodegaProductoService bodegaProductoService;

    @PostMapping
    public ResponseEntity<BodegaProductoResponse> crear(@Valid @RequestBody BodegaProductoRequest request) {
        BodegaProductoResponse creado = bodegaProductoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<BodegaProductoResponse>> obtenerTodos() {
        return ResponseEntity.ok(bodegaProductoService.obtenerTodos());
    }

    @GetMapping("/{bodegaId}/{productoId}")
    public ResponseEntity<BodegaProductoResponse> buscarPorId(
            @PathVariable Long bodegaId,
            @PathVariable Long productoId) {
        return ResponseEntity.ok(bodegaProductoService.buscarPorId(bodegaId, productoId));
    }

    @PutMapping("/{bodegaId}/{productoId}")
    public ResponseEntity<BodegaProductoResponse> actualizarStock(
            @PathVariable Long bodegaId,
            @PathVariable Long productoId,
            @Valid @RequestBody StockUpdateRequest request) {
        return ResponseEntity.ok(bodegaProductoService.actualizarStock(bodegaId, productoId, request.stock()));
    }

    @DeleteMapping("/{bodegaId}/{productoId}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long bodegaId,
            @PathVariable Long productoId) {
        bodegaProductoService.eliminar(bodegaId, productoId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/stock-bajo")
    public ResponseEntity<List<BodegaProductoResponse>> obtenerStockBajo(
            @RequestParam(defaultValue = "10") int umbral) {
        return ResponseEntity.ok(bodegaProductoService.obtenerStockBajo(umbral));
    }
}