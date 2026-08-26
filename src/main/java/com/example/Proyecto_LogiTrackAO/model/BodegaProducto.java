package com.example.Proyecto_LogiTrackAO.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bodega_producto")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class BodegaProducto {

    @EmbeddedId
    private BodegaProductoId id;

    @ManyToOne
    @MapsId("bodegaId")
    @JoinColumn(name = "bodega_id")
    private Bodega bodega;

    @ManyToOne
    @MapsId("productoId")
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Min(0)
    @Column(nullable = false)
    private int stock;
}