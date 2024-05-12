package com.farmacia.org.ProySpringFarmacia.repository;

import com.farmacia.org.ProySpringFarmacia.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClienteRepository extends CrudRepository<Cliente, Integer> {

    List<Cliente> findAllByNombresStartingWith(String nombres);
    List<Cliente> findAllByApellidosStartingWith(String apellidos);

}
