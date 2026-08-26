package com.example.Proyecto_LogiTrackAO.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bodegas")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Bodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String ubicacion;

    @Min(0)
    @Column(nullable = false)
    private int capacidad;

    @ManyToOne
    @JoinColumn(name = "encargado_id", nullable = true)
    private Usuario encargado;
}