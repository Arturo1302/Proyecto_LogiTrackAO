package com.example.Proyecto_LogiTrackAO.controller;

import com.example.Proyecto_LogiTrackAO.dto.response.AuditoriaResponse;
import com.example.Proyecto_LogiTrackAO.model.TipoOperacion;
import com.example.Proyecto_LogiTrackAO.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Auditoría", description = "Consulta del historial de cambios registrado automáticamente por triggers SQL")
@RestController
@RequestMapping("/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @Operation(summary = "Lista todas las auditorías",
            description = "Devuelve el historial completo de operaciones INSERT/UPDATE/DELETE auditadas.")
    @GetMapping
    public ResponseEntity<List<AuditoriaResponse>> obtenerTodas() {
        return ResponseEntity.ok(auditoriaService.obtenerTodas());
    }

    @Operation(summary = "Busca una auditoría por id",
            description = "Devuelve el detalle de un registro de auditoría específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Auditoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaResponse> buscarPorId(
            @Parameter(description = "Id del registro de auditoría", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.buscarPorId(id));
    }

    @Operation(summary = "Filtra auditorías por usuario de base de datos",
            description = "Devuelve los registros de auditoría generados por un usuario de MySQL específico.")
    @GetMapping("/por-usuario")
    public ResponseEntity<List<AuditoriaResponse>> obtenerPorUsuario(
            @Parameter(description = "Usuario de MySQL", example = "root@localhost")
            @RequestParam String usuario) {
        return ResponseEntity.ok(auditoriaService.obtenerPorUsuario(usuario));
    }

    @Operation(summary = "Filtra auditorías por tipo de operación",
            description = "Devuelve los registros de auditoría de un tipo específico: INSERT, UPDATE o DELETE.")
    @GetMapping("/por-operacion")
    public ResponseEntity<List<AuditoriaResponse>> obtenerPorOperacion(
            @Parameter(description = "Tipo de operación", example = "UPDATE")
            @RequestParam TipoOperacion operacion) {
        return ResponseEntity.ok(auditoriaService.obtenerPorOperacion(operacion));
    }
}