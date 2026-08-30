package com.example.Proyecto_LogiTrackAO.controller;

import com.example.Proyecto_LogiTrackAO.dto.response.AuditoriaResponse;
import com.example.Proyecto_LogiTrackAO.model.TipoOperacion;
import com.example.Proyecto_LogiTrackAO.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<AuditoriaResponse>> obtenerTodas() {
        return ResponseEntity.ok(auditoriaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.buscarPorId(id));
    }

    @GetMapping("/por-usuario")
    public ResponseEntity<List<AuditoriaResponse>> obtenerPorUsuario(@RequestParam String usuario) {
        return ResponseEntity.ok(auditoriaService.obtenerPorUsuario(usuario));
    }

    @GetMapping("/por-operacion")
    public ResponseEntity<List<AuditoriaResponse>> obtenerPorOperacion(@RequestParam TipoOperacion operacion) {
        return ResponseEntity.ok(auditoriaService.obtenerPorOperacion(operacion));
    }
}