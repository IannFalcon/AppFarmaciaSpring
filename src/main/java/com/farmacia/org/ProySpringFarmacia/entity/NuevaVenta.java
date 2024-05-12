package com.farmacia.org.ProySpringFarmacia.entity;

import lombok.Data;

@Data
public class NuevaVenta {

    private Integer idNuevaVenta;
    private Integer idEmpleado;
    private Integer idCliente;
    private Integer idProducto;
    private Integer cantidad;

}
