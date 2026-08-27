// mapper/BodegaProductoMapper.java
package com.example.Proyecto_LogiTrackAO.mapper;

import com.example.Proyecto_LogiTrackAO.dto.response.BodegaProductoResponse;
import com.example.Proyecto_LogiTrackAO.model.Bodega;
import com.example.Proyecto_LogiTrackAO.model.BodegaProducto;
import com.example.Proyecto_LogiTrackAO.model.BodegaProductoId;
import com.example.Proyecto_LogiTrackAO.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class BodegaProductoMapper {

    public BodegaProducto dtoToEntity(Bodega bodega, Producto producto, int stock) {
        BodegaProducto bp = new BodegaProducto();
        bp.setId(new BodegaProductoId(bodega.getId(), producto.getId()));
        bp.setBodega(bodega);
        bp.setProducto(producto);
        bp.setStock(stock);
        return bp;
    }

    public BodegaProductoResponse entityToDto(BodegaProducto bp) {
        return new BodegaProductoResponse(
                bp.getBodega().getId(),
                bp.getBodega().getNombre(),
                bp.getProducto().getId(),
                bp.getProducto().getNombre(),
                bp.getStock()
        );
    }
}