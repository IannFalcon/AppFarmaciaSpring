package com.farmacia.org.ProySpringFarmacia.repository;

import com.farmacia.org.ProySpringFarmacia.entity.Venta;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VentaRepository extends CrudRepository<Venta, Integer> {

    List<Venta> findAllByIdEmpleado(@Param("idEmpleado") Integer idEmpleado);
    List<Venta> findAllByIdCliente(@Param("idCliente") Integer idCliente);

    @Query("select v from Venta v where v.detalles is empty")
    List<Venta> findVentasSinDetalles();

    @Modifying
    @Query("delete from Venta v where v.idEmpleado = :idEmpleado")
    void deleteByIdEmpleado(@Param("idEmpleado") Integer idEmpleado);

    @Modifying
    @Query("delete from Venta v where v.idCliente = :idCliente")
    void deleteByIdCliente(@Param("idCliente") Integer idCliente);

}
