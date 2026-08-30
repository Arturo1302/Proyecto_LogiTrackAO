package com.example.Proyecto_LogiTrackAO.repository;

import com.example.Proyecto_LogiTrackAO.model.Bodega;
import com.example.Proyecto_LogiTrackAO.model.BodegaProducto;
import com.example.Proyecto_LogiTrackAO.model.BodegaProductoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BodegaProductoRepository extends JpaRepository<BodegaProducto, BodegaProductoId> {


    List<BodegaProducto> findByStockLessThan(int stock);
}
