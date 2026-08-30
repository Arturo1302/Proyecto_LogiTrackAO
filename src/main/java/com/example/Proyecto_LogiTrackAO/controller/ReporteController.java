package com.example.Proyecto_LogiTrackAO.controller;

import com.example.Proyecto_LogiTrackAO.dto.response.ReporteGeneralResponse;
import com.example.Proyecto_LogiTrackAO.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Reportes", description = "Reportes generales agregados del sistema")
@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @Operation(summary = "Reporte general del sistema",
            description = "Devuelve el stock total agrupado por bodega y el top 5 de productos más movidos en la historia.")
    @GetMapping("/general")
    public ResponseEntity<ReporteGeneralResponse> obtenerReporteGeneral() {
        return ResponseEntity.ok(reporteService.obtenerReporteGeneral());
    }
}