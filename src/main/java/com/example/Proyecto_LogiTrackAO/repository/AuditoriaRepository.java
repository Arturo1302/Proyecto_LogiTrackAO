package com.example.Proyecto_LogiTrackAO.repository;

import com.example.Proyecto_LogiTrackAO.model.Auditoria;
import com.example.Proyecto_LogiTrackAO.model.TipoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    // AuditoriaRepository
    List<Auditoria> findByUsuario(String usuario);
    List<Auditoria> findByOperacion(TipoOperacion operacion);
}
