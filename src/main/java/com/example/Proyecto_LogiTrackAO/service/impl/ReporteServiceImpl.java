// service/impl/ReporteServiceImpl.java
package com.example.Proyecto_LogiTrackAO.service.impl;

import com.example.Proyecto_LogiTrackAO.dto.response.ReporteGeneralResponse;
import com.example.Proyecto_LogiTrackAO.repository.BodegaProductoRepository;
import com.example.Proyecto_LogiTrackAO.repository.DetalleMovimientoRepository;
import com.example.Proyecto_LogiTrackAO.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final BodegaProductoRepository bodegaProductoRepository;
    private final DetalleMovimientoRepository detalleMovimientoRepository;

    @Override
    public ReporteGeneralResponse obtenerReporteGeneral() {
        var stockPorBodega = bodegaProductoRepository.obtenerStockTotalPorBodega();
        var productosMasMovidos = detalleMovimientoRepository.obtenerProductosMasMovidos(PageRequest.of(0, 5));
        return new ReporteGeneralResponse(stockPorBodega, productosMasMovidos);
    }
}