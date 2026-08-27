// mapper/ProductoMapper.java
package com.example.Proyecto_LogiTrackAO.mapper;

import com.example.Proyecto_LogiTrackAO.dto.request.ProductoRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.ProductoResponse;
import com.example.Proyecto_LogiTrackAO.model.Categoria;
import com.example.Proyecto_LogiTrackAO.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto dtoToEntity(ProductoRequest request, Categoria categoria) {
        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setCategoria(categoria);
        producto.setPrecio(request.precio());
        return producto;
    }

    public ProductoResponse entityToDto(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria().getNombre(),
                producto.getPrecio()
        );
    }
}