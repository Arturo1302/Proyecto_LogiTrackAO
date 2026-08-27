package com.example.Proyecto_LogiTrackAO.mapper;

import com.example.Proyecto_LogiTrackAO.dto.request.CategoriaRequest;
import com.example.Proyecto_LogiTrackAO.dto.response.CategoriaResponse;
import com.example.Proyecto_LogiTrackAO.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria dtoToEntity(CategoriaRequest categoriaRequest){
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaRequest.nombre());
        categoria.setDescripcion(categoriaRequest.descripcion());
        return categoria;
    }

    public CategoriaResponse entityToDto(Categoria categoria){
        return new CategoriaResponse( categoria.getId(), categoria.getNombre(), categoria.getDescripcion());
    }
}
