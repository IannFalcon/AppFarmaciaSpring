package com.farmacia.org.ProySpringFarmacia.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;

    private Integer idVenta;
    private Integer idProducto;
    private Integer cantidad;
    private Double precioUnitario;

}
