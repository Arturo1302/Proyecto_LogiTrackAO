// service/impl/ProductoServiceImpl.java
package com.example.Proyecto_LogiTrackAO.service.impl;

import com.example.Proyecto_LogiTrackAO.dto.request.ProductoRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.ProductoResponse;
import com.example.Proyecto_LogiTrackAO.mapper.ProductoMapper;
import com.example.Proyecto_LogiTrackAO.model.Categoria;
import com.example.Proyecto_LogiTrackAO.model.Producto;
import com.example.Proyecto_LogiTrackAO.repository.CategoriaRepository;
import com.example.Proyecto_LogiTrackAO.repository.ProductoRepository;
import com.example.Proyecto_LogiTrackAO.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;

    @Override
    public ProductoResponse crearProducto(ProductoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.categoriaId()));
        Producto producto = productoMapper.dtoToEntity(request, categoria);
        Producto guardado = productoRepository.save(producto);
        return productoMapper.entityToDto(guardado);
    }

    @Override
    public ProductoResponse actualizarProducto(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.categoriaId()));
        producto.setNombre(request.nombre());
        producto.setCategoria(categoria);
        producto.setPrecio(request.precio());
        Producto actualizado = productoRepository.save(producto);
        return productoMapper.entityToDto(actualizado);
    }

    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public List<ProductoResponse> obtenerProductos() {
        return productoRepository.findAll().stream()
                .map(productoMapper::entityToDto)
                .toList();
    }

    @Override
    public ProductoResponse buscarPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        return productoMapper.entityToDto(producto);
    }
}