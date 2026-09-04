package com.example.Proyecto_LogiTrackAO.controller;

import com.example.Proyecto_LogiTrackAO.dto.request.BodegaRequest;
import com.example.Proyecto_LogiTrackAO.dto.request.ProductoRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.BodegaResponse;
import com.example.Proyecto_LogiTrackAO.dto.response.ProductoResponse;
import com.example.Proyecto_LogiTrackAO.model.Bodega;
import com.example.Proyecto_LogiTrackAO.service.BodegaService;
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

@Tag(name = "Bodega", description = "Gestión de bodegas de almacenamiento")
@RestController
@RequestMapping("/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaService bodegaService;

    @Operation(summary = "Crea una bodega",
            description = "Registra una nueva bodega, opcionalmente con un usuario encargado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bodega creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario encargado no encontrado")
    })
    @PostMapping
    public ResponseEntity<BodegaResponse> crear(@Valid @RequestBody BodegaRequest request) {
        BodegaResponse creada = bodegaService.crearBodega(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Lista todas las bodegas",
            description = "Devuelve el listado completo de bodegas registradas.")
    @GetMapping
    public ResponseEntity<List<BodegaResponse>> obtenerTodas() {
        return ResponseEntity.ok(bodegaService.obtenerBodegas());
    }


    @PostMapping
    public ResponseEntity<BodegaResponse> crear(@Valid @RequestBody BodegaRequest request) {
        BodegaResponse creado = bodegaService.crearbodega( request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
    @Operation(summary = "Busca una bodega por id",
            description = "Devuelve los datos de una bodega específica, incluyendo su encargado si tiene.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega encontrada"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponse> buscarPorId(
            @Parameter(description = "Id de la bodega", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.buscarPorId(id));
    }

    @Operation(summary = "Actualiza una bodega",
            description = "Modifica los datos de una bodega existente, incluyendo su encargado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega actualizada"),
            @ApiResponse(responseCode = "404", description = "Bodega o usuario encargado no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BodegaResponse> actualizar(
            @PathVariable Long id, @Valid @RequestBody BodegaRequest request) {
        return ResponseEntity.ok(bodegaService.actualizarBodega(id, request));
    }

    @Operation(summary = "Elimina una bodega",
            description = "Elimina una bodega existente por su id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bodega eliminada"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Id de la bodega a eliminar", example = "1") @PathVariable Long id) {
        bodegaService.eliminarBodega(id);
        return ResponseEntity.noContent().build();
    }
}