package com.farmacia.org.ProySpringFarmacia.repository;

import com.farmacia.org.ProySpringFarmacia.entity.DetalleVenta;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetalleVentaRepository extends CrudRepository<DetalleVenta, Integer> {

    List<DetalleVenta> findAllByIdVenta(@Param("idVenta") Integer idVenta);
    List<DetalleVenta> findAllByIdProducto(@Param("idProducto") Integer idProducto);

    @Modifying
    @Query("delete from DetalleVenta dv where dv.idVenta = :idVenta")
    void deleteByIdVenta(@Param("idVenta") Integer idVenta);

    @Modifying
    @Query("delete from DetalleVenta dv where dv.idProducto = :idProducto")
    void deleteByIdProducto(@Param("idProducto") Integer idProducto);

}
