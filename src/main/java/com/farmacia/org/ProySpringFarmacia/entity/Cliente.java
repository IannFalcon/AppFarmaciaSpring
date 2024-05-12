package com.farmacia.org.ProySpringFarmacia.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;

    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono;

}