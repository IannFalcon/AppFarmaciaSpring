package com.farmacia.org.ProySpringFarmacia.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmpleado;

    private String nombre;
    private String apellido;
    private String dni;
    private String direccion;
    private String correo;
    private String cargo;

    private String username;
    private String password;

    @OneToMany(mappedBy = "idEmpleado", cascade = CascadeType.REMOVE)
    private List<Venta> ventas;

}
