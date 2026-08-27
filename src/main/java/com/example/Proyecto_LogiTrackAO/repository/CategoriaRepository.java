package com.example.Proyecto_LogiTrackAO.repository;

import com.example.Proyecto_LogiTrackAO.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

    Optional<Categoria> findByNombre(String nombre);

}
