package com.example.Proyecto_LogiTrackAO.controller;

import com.example.Proyecto_LogiTrackAO.dto.request.BodegaProductoRequest;
import com.example.Proyecto_LogiTrackAO.dto.request.StockUpdateRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.BodegaProductoResponse;
import com.example.Proyecto_LogiTrackAO.service.BodegaProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Stock por Bodega", description = "Gestión del stock de productos dentro de cada bodega")
@RestController
@RequestMapping("/bodega-producto")
@RequiredArgsConstructor
public class BodegaProductoController {

    private final BodegaProductoService bodegaProductoService;

    @Operation(summary = "Registra stock inicial",
            description = "Crea el registro de stock de un producto en una bodega específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Bodega o producto no encontrado")
    })
    @PostMapping
    public ResponseEntity<BodegaProductoResponse> crear(@Valid @RequestBody BodegaProductoRequest request) {
        BodegaProductoResponse creado = bodegaProductoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Lista todo el stock",
            description = "Devuelve todos los registros de stock de productos en todas las bodegas.")
    @GetMapping
    public ResponseEntity<List<BodegaProductoResponse>> obtenerTodos() {
        return ResponseEntity.ok(bodegaProductoService.obtenerTodos());
    }

    @Operation(summary = "Consulta productos con stock bajo",
            description = "Devuelve los registros cuyo stock esté por debajo del umbral indicado (10 unidades por defecto).")
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<BodegaProductoResponse>> obtenerStockBajo(
            @Parameter(description = "Cantidad mínima de referencia", example = "10")
            @RequestParam(defaultValue = "10") int umbral) {
        return ResponseEntity.ok(bodegaProductoService.obtenerStockBajo(umbral));
    }

    @Operation(summary = "Busca el stock de un producto en una bodega",
            description = "Devuelve el registro de stock para la combinación exacta de bodega y producto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @GetMapping("/{bodegaId}/{productoId}")
    public ResponseEntity<BodegaProductoResponse> buscarPorId(
            @Parameter(description = "Id de la bodega", example = "1") @PathVariable Long bodegaId,
            @Parameter(description = "Id del producto", example = "1") @PathVariable Long productoId) {
        return ResponseEntity.ok(bodegaProductoService.buscarPorId(bodegaId, productoId));
    }

    @Operation(summary = "Actualiza el stock",
            description = "Ajusta manualmente la cantidad de stock de un producto en una bodega.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{bodegaId}/{productoId}")
    public ResponseEntity<BodegaProductoResponse> actualizarStock(
            @Parameter(description = "Id de la bodega", example = "1") @PathVariable Long bodegaId,
            @Parameter(description = "Id del producto", example = "1") @PathVariable Long productoId,
            @Valid @RequestBody StockUpdateRequest request) {
        return ResponseEntity.ok(bodegaProductoService.actualizarStock(bodegaId, productoId, request.stock()));
    }

    @Operation(summary = "Elimina un registro de stock",
            description = "Elimina el registro de stock de un producto en una bodega.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro eliminado"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @DeleteMapping("/{bodegaId}/{productoId}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Id de la bodega", example = "1") @PathVariable Long bodegaId,
            @Parameter(description = "Id del producto", example = "1") @PathVariable Long productoId) {
        bodegaProductoService.eliminar(bodegaId, productoId);
        return ResponseEntity.noContent().build();
    }
}