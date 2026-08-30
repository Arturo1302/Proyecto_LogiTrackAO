package com.example.Proyecto_LogiTrackAO.controller;

import com.example.Proyecto_LogiTrackAO.dto.request.MovimientoRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.MovimientoResponse;
import com.example.Proyecto_LogiTrackAO.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Movimiento", description = "Registro de entradas, salidas y transferencias de inventario")
@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @Operation(summary = "Registra un movimiento de inventario",
            description = "Crea una ENTRADA, SALIDA o TRANSFERENCIA con sus productos y cantidades. " +
                    "Valida stock suficiente y bodegas requeridas según el tipo antes de guardar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio violada (ej: stock insuficiente)"),
            @ApiResponse(responseCode = "404", description = "Usuario, bodega o producto no encontrado")
    })
    @PostMapping
    public ResponseEntity<MovimientoResponse> registrar(@Valid @RequestBody MovimientoRequest request) {
        MovimientoResponse creado = movimientoService.registrarMovimiento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Lista todos los movimientos",
            description = "Devuelve el historial completo de movimientos registrados.")
    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> obtenerTodos() {
        return ResponseEntity.ok(movimientoService.obtenerTodos());
    }

    @Operation(summary = "Busca un movimiento por id",
            description = "Devuelve el detalle completo de un movimiento, incluyendo sus productos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> buscarPorId(
            @Parameter(description = "Id del movimiento", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.buscarPorId(id));
    }

    @Operation(summary = "Filtra movimientos por rango de fechas",
            description = "Devuelve los movimientos ocurridos entre las fechas indicadas (inclusive todo el día).")
    @GetMapping("/por-fecha")
    public ResponseEntity<List<MovimientoResponse>> obtenerPorRangoFechas(
            @Parameter(description = "Fecha inicial", example = "2026-08-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Fecha final", example = "2026-08-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        LocalDateTime inicioDelDia = inicio.atStartOfDay();
        LocalDateTime finDelDia = fin.atTime(23, 59, 59);
        return ResponseEntity.ok(movimientoService.obtenerPorRangoFechas(inicioDelDia, finDelDia));
    }
}