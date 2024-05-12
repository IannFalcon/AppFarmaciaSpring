package com.farmacia.org.ProySpringFarmacia.service;

import com.farmacia.org.ProySpringFarmacia.entity.Cliente;
import com.farmacia.org.ProySpringFarmacia.entity.Venta;
import com.farmacia.org.ProySpringFarmacia.repository.ClienteRepository;
import com.farmacia.org.ProySpringFarmacia.repository.DetalleVentaRepository;
import com.farmacia.org.ProySpringFarmacia.repository.VentaRepository;
import com.farmacia.org.ProySpringFarmacia.response.CrudResponse;
import com.farmacia.org.ProySpringFarmacia.response.FindClienteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Farmacia")
public class ClienteService {

    // Inyeccion de dependencias
    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    DetalleVentaRepository detalleVentaRepository;

    // Listar Clientes
    @GetMapping("/cliente/all")
    public FindClienteResponse findCliente(@RequestParam(value = "id", defaultValue = "0") Integer id){

        Iterable<Cliente> clientes = null;

        if(id > 0){
            clientes = clienteRepository.findAllById(List.of(id));
        } else {
            clientes = clienteRepository.findAll();
        }

        return new FindClienteResponse("01", "Clientes encontrados", clientes);

    }

    // Listar Clientes Por Nombre
    @GetMapping("/cliente/nombres")
    public FindClienteResponse findClienteByNombres(@RequestParam(value = "nombres", defaultValue = "") String nombres){

        Iterable<Cliente> clientes = null;

        if(nombres != null && !nombres.isEmpty()){
            clientes = clienteRepository.findAllByNombresStartingWith(nombres);
        } else {
            clientes = clienteRepository.findAll();
        }

        return new FindClienteResponse("01", "Clientes encontrados", clientes);

    }

    // Listar Clientes Por Apellido
    @GetMapping("/cliente/apellidos")
    public FindClienteResponse findClienteByApellidos(@RequestParam(value = "apellidos", defaultValue = "") String apellidos){

        Iterable<Cliente> clientes = null;

        if(apellidos != null && !apellidos.isEmpty()){
            clientes = clienteRepository.findAllByApellidosStartingWith(apellidos);
        } else {
            clientes = clienteRepository.findAll();
        }

        return new FindClienteResponse("01", "Clientes encontrados", clientes);

    }

    // Registrar Cliente
    @PostMapping("/cliente/add")
    public CrudResponse addCliente(@RequestBody Cliente cliente){

        if(cliente.getIdCliente() != null){
            return new CrudResponse("99", "Parametro ID no permitido.");
        }

        clienteRepository.save(cliente);
        return new CrudResponse("01", "Cliente registrado con exito.");

    }

    // Actualizar Cliente
    @PostMapping("/cliente/update")
    public CrudResponse updateCliente(@RequestBody Cliente cliente){

        if(clienteRepository.findById(cliente.getIdCliente()).isEmpty()){
            return new CrudResponse("99", "Cliente no encontrado.");
        }

        clienteRepository.save(cliente);
        return new CrudResponse("01", "Cliente actualizado.");

    }

    // Eliminar Cliente
    @Transactional
    @PostMapping("/cliente/delete")
    public CrudResponse deleteCliente(@RequestParam(value = "id", defaultValue = "0") Integer id){

        if(clienteRepository.findById(id).isEmpty()){
            return new CrudResponse("99", "Cliente no encontrado.");
        }

        // Traer la lista de ventas por el ID del Cliente
        List<Venta> ventas = ventaRepository.findAllByIdCliente(id);

        // Si la lista de ventas no es nula
        if(ventas != null){
            // Eliminar los detalles de venta asociados a la venta
            for(Venta venta : ventas){
                detalleVentaRepository.deleteByIdVenta(venta.getIdVenta());
            }
        }

        // Eliminar las ventas asociadas al cliente
        ventaRepository.deleteByIdCliente(id);

        // Eliminar al cliente
        clienteRepository.deleteById(id);

        return new CrudResponse("01", "Cliente eliminado.");

    }

}
