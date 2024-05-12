package com.farmacia.org.ProySpringFarmacia.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProveedor;

    private String nombre;
    private String direccion;
    private String telefono;

    @OneToMany(mappedBy = "idProveedor", cascade = CascadeType.REMOVE)
    private List<Producto> productos;

}
