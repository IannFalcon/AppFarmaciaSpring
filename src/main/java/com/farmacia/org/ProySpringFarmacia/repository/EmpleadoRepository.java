package com.farmacia.org.ProySpringFarmacia.repository;

import com.farmacia.org.ProySpringFarmacia.entity.Empleado;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmpleadoRepository extends CrudRepository<Empleado, Integer> {

    Empleado findByUsername(String username);
    List<Empleado> findAllByNombreStartingWith(String nombre);

}