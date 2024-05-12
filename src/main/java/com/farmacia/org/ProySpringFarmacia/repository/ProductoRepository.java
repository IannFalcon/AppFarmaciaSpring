package com.farmacia.org.ProySpringFarmacia.repository;

import com.farmacia.org.ProySpringFarmacia.entity.Producto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends CrudRepository<Producto, Integer> {

    List<Producto> findAllByNombreStartingWith(@Param("nombre") String nombre);
    List<Producto> findAllByIdProveedor(@Param("idProveedor") Integer idProveedor);

}
