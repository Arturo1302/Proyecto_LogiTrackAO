package com.example.Proyecto_LogiTrackAO.repository;

import com.example.Proyecto_LogiTrackAO.model.Bodega;
import com.example.Proyecto_LogiTrackAO.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BodegaRepository extends JpaRepository<Bodega, Long> {

    List<Bodega> obtenerTodas(String nombre);
    List<Bodega> findByNombre(String nombre);
    List<Bodega> findByUbicacion(String ubicacion);
    List<Bodega> findByEncargado(Usuario encargado);


}
