package com.example.Proyecto_LogiTrackAO.repository;

import com.example.Proyecto_LogiTrackAO.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto,Long> {

    List<Producto> obtenerProductos(Producto producto);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByCategoriaContainingIgnoreCase(String categoria);
    List<Producto> findByPrecioGreaterThan(BigDecimal precio);
    List<Producto> findByPrecioLessThan(BigDecimal precio);
}
