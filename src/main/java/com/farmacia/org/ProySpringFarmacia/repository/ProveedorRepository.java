package com.farmacia.org.ProySpringFarmacia.repository;

import com.farmacia.org.ProySpringFarmacia.entity.Proveedor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProveedorRepository extends CrudRepository<Proveedor, Integer> {

    List<Proveedor> findAllByNombreStartingWith(String nombre);

}
