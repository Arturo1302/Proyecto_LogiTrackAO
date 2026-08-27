// service/ProductoService.java
package com.example.Proyecto_LogiTrackAO.service;

import com.example.Proyecto_LogiTrackAO.dto.request.ProductoRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.ProductoResponse;

import java.util.List;

public interface ProductoService {
    ProductoResponse crearProducto(ProductoRequest request);
    ProductoResponse actualizarProducto(Long id, ProductoRequest request);
    void eliminarProducto(Long id);
    List<ProductoResponse> obtenerProductos();
    ProductoResponse buscarPorId(Long id);
}