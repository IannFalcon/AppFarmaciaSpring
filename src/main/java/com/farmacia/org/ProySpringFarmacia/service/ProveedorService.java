package com.farmacia.org.ProySpringFarmacia.service;

import com.farmacia.org.ProySpringFarmacia.entity.*;
import com.farmacia.org.ProySpringFarmacia.repository.DetalleVentaRepository;
import com.farmacia.org.ProySpringFarmacia.repository.ProductoRepository;
import com.farmacia.org.ProySpringFarmacia.repository.ProveedorRepository;
import com.farmacia.org.ProySpringFarmacia.repository.VentaRepository;
import com.farmacia.org.ProySpringFarmacia.response.CrudResponse;
import com.farmacia.org.ProySpringFarmacia.response.FindProveedorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Farmacia")
public class ProveedorService {

    // Inyeccion de dependecias
    @Autowired
    ProveedorRepository proveedorRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    DetalleVentaRepository detalleVentaRepository;

    // Listar Proveedores
    @GetMapping("/proveedor/all")
    public FindProveedorResponse findProveedor(@RequestParam(value = "id", defaultValue = "0") Integer id){

        Iterable<Proveedor> proveedores = null;

        if(id > 0){
            proveedores = proveedorRepository.findAllById(List.of(id));
        } else {
            proveedores = proveedorRepository.findAll();
        }

        return new FindProveedorResponse("01", "Proveedores encontrados.", proveedores);

    }

    // Listar Proveedor por nombre
    @GetMapping("/proveedor/nombre")
    public FindProveedorResponse findProveedor(@RequestParam(value = "nombre", defaultValue = "") String nombre){

        Iterable<Proveedor> proveedores = null;

        if(nombre != null && !nombre.isEmpty()){
            proveedores = proveedorRepository.findAllByNombreStartingWith(nombre);
        } else {
            proveedores = proveedorRepository.findAll();
        }

        return new FindProveedorResponse("01", "Proveedores encontrados.", proveedores);

    }

    // Registrar Proveedor
    @PostMapping("/proveedor/add")
    public CrudResponse addProveedor(@RequestBody Proveedor proveedor){

        if(proveedor.getIdProveedor() != null){
            return new CrudResponse("99", "Parametro ID no permitido");
        }

        proveedorRepository.save(proveedor);
        return new CrudResponse("01", "Proveedor registrado con exito.");

    }

    // Actualizar Proveedor
    @PostMapping("/proveedor/update")
    public CrudResponse updateProveedor(@RequestBody Proveedor proveedor){

        if(proveedorRepository.findById(proveedor.getIdProveedor()).isEmpty()){
            return new CrudResponse("99", "Proveedor no encontrado.");
        }

        proveedorRepository.save(proveedor);
        return new CrudResponse("01", "Proveedor actualizado");

    }

    // Eliminar Proveedor
    @Transactional
    @PostMapping("/proveedor/delete")
    public CrudResponse deleteProveedor(@RequestParam(value = "id", defaultValue = "0") Integer id){

        if(proveedorRepository.findById(id).isEmpty()){
            return new CrudResponse("99", "Proveedor no encontrado.");
        }

        // Traer la lista de productos por el ID del Proveedor
        List<Producto> productos = productoRepository.findAllByIdProveedor(id);

        // Si la lista de productos no es nula
        if(productos != null){
            // Eliminar los detalles de venta asociados al producto
            for(Producto producto : productos){
                detalleVentaRepository.deleteByIdProducto(producto.getIdProducto());
                productoRepository.deleteById(producto.getIdProducto());
            }
        }

        // Traer las ventas que no tienen detalles de venta asociados
        List<Venta> ventas = ventaRepository.findVentasSinDetalles();

        // Eliminar las ventas que no tienen detalles de venta asociados
        for(Venta venta : ventas){
            ventaRepository.deleteById(venta.getIdVenta());
        }

        // Eliminar el proveedor
        proveedorRepository.deleteById(id);

        return new CrudResponse("01", "Proveedor eliminado.");

    }

}
