package com.example.Proyecto_LogiTrackAO.repository;

import com.example.Proyecto_LogiTrackAO.model.Rol;
import com.example.Proyecto_LogiTrackAO.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRol(Rol rol);
}
