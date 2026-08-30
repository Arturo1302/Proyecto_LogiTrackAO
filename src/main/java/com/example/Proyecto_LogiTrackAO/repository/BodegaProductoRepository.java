package com.example.Proyecto_LogiTrackAO.repository;

import com.example.Proyecto_LogiTrackAO.dto.response.StockPorBodegaResponse;
import com.example.Proyecto_LogiTrackAO.model.Bodega;
import com.example.Proyecto_LogiTrackAO.model.BodegaProducto;
import com.example.Proyecto_LogiTrackAO.model.BodegaProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BodegaProductoRepository extends JpaRepository<BodegaProducto, BodegaProductoId> {


    List<BodegaProducto> findByStockLessThan(int stock);


    @Query("""
    SELECT new com.example.Proyecto_LogiTrackAO.dto.response.StockPorBodegaResponse(
        b.id, b.nombre, SUM(bp.stock))
    FROM BodegaProducto bp
    JOIN bp.bodega b
    GROUP BY b.id, b.nombre
    """)
    List<StockPorBodegaResponse> obtenerStockTotalPorBodega();
}
