package com.farmacia.org.ProySpringFarmacia.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVenta;
    private Integer idEmpleado;
    private Integer idCliente;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date fechaVenta;

    private Double importeTotal;

    @OneToMany(mappedBy = "idVenta", cascade = CascadeType.REMOVE)
    private List<DetalleVenta> detalles;

}