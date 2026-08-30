package com.example.Proyecto_LogiTrackAO.controller;

import com.example.Proyecto_LogiTrackAO.dto.request.RolRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.RolResponse;
import com.example.Proyecto_LogiTrackAO.service.RolService;
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

@Tag(name = "Rol", description = "Gestión de roles del sistema (ADMIN, EMPLEADO)")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @Operation(summary = "Crea un nuevo rol",
            description = "Registra un nuevo rol en el sistema. El nombre debe ser único (ej: ADMIN, EMPLEADO).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<RolResponse> crear(@Valid @RequestBody RolRequest request) {
        RolResponse creado = rolService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Lista todos los roles",
            description = "Devuelve el listado completo de roles registrados en el sistema.")
    @GetMapping
    public ResponseEntity<List<RolResponse>> listarTodos() {
        return ResponseEntity.ok(rolService.listarTodos());
    }

    @Operation(summary = "Busca un rol por su id",
            description = "Devuelve los datos de un rol específico a partir de su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RolResponse> buscarPorId(
            @Parameter(description = "Id del rol", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(rolService.buscarPorId(id));
    }

    @Operation(summary = "Actualiza un rol existente",
            description = "Modifica el nombre de un rol ya registrado en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RolResponse> actualizar(
            @Parameter(description = "Id del rol a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody RolRequest request) {
        return ResponseEntity.ok(rolService.actualizar(id, request));
    }

    @Operation(summary = "Elimina un rol",
            description = "Elimina un rol existente por su id. No debería usarse si hay usuarios asociados a él.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rol eliminado"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Id del rol a eliminar", example = "1")
            @PathVariable Long id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}