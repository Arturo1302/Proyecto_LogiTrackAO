package com.example.Proyecto_LogiTrackAO.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode
public class BodegaProductoId implements Serializable {

    private Integer bodegaId;
    private Integer productoId;
}