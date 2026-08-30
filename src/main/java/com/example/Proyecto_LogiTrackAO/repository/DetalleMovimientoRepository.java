package com.example.Proyecto_LogiTrackAO.repository;

import com.example.Proyecto_LogiTrackAO.dto.response.ProductoMasMovidoResponse;
import com.example.Proyecto_LogiTrackAO.model.DetalleMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface DetalleMovimientoRepository extends JpaRepository<DetalleMovimiento, Integer> {


    List<DetalleMovimiento> findByMovimientoId(Long movimientoId);


    @Query("""
    SELECT new com.example.Proyecto_LogiTrackAO.dto.response.ProductoMasMovidoResponse(
        p.id, p.nombre, SUM(dm.cantidad))
    FROM DetalleMovimiento dm
    JOIN dm.producto p
    GROUP BY p.id, p.nombre
    ORDER BY SUM(dm.cantidad) DESC
    """)
    List<ProductoMasMovidoResponse> obtenerProductosMasMovidos(Pageable pageable);
}
