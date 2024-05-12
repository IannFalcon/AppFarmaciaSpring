package com.farmacia.org.ProySpringFarmacia.service;

import com.farmacia.org.ProySpringFarmacia.entity.*;
import com.farmacia.org.ProySpringFarmacia.repository.DetalleVentaRepository;
import com.farmacia.org.ProySpringFarmacia.repository.ProductoRepository;
import com.farmacia.org.ProySpringFarmacia.repository.ProveedorRepository;
import com.farmacia.org.ProySpringFarmacia.repository.VentaRepository;
import com.farmacia.org.ProySpringFarmacia.response.CrudResponse;
import com.farmacia.org.ProySpringFarmacia.response.FindProductoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Farmacia")
public class ProductoService {

    // Inyecciones de dependencia
    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    ProveedorRepository proveedorRepository;

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    DetalleVentaRepository detalleVentaRepository;

    // Listar Productos
    @GetMapping("/producto/all")
    public FindProductoResponse findProducto(@RequestParam(value = "id", defaultValue = "0") Integer id){

        Iterable<Producto> productos = null;

        if(id > 0){
            productos = productoRepository.findAllById(List.of(id));
        } else {
            productos = productoRepository.findAll();
        }

        return new FindProductoResponse("01", "Productos encontrados.", productos);

    }

    // Lista Productos Por Nombre
    @GetMapping("/producto/nombre")
    public FindProductoResponse findProductoByNombre(@RequestParam(value = "nombre", defaultValue = "") String nombre){

        Iterable<Producto> productos = null;

        if(nombre != null && !nombre.isEmpty()){
            productos = productoRepository.findAllByNombreStartingWith(nombre);
        } else {
            productos = productoRepository.findAll();
        }

        return new FindProductoResponse("01", "Productos encontrados.", productos);

    }

    // Registrar Producto
    @PostMapping("/producto/add")
    public CrudResponse addProducto(@RequestBody Producto producto) {

        if(producto.getIdProducto() != null){
            return new CrudResponse("99", "Parametro ID no permitido.");
        }

        // Verificar si el proveedor existe
        Optional<Proveedor> clienteOptional = proveedorRepository.findById(producto.getIdProveedor());
        if (clienteOptional.isEmpty()) {
            return new CrudResponse("99", "Proveedor no encontrado");
        }

        productoRepository.save(producto);
        return new CrudResponse("01", "Producto registrado correctamente.");

    }

    // Actualizar Producto
    @PostMapping("/producto/update")
    public CrudResponse updateProducto(@RequestBody Producto producto){

        if(productoRepository.findById(producto.getIdProducto()).isEmpty()){
            return new CrudResponse("99", "Producto no encontrado.");
        }

        // Verificar si el proveedor existe
        Optional<Proveedor> clienteOptional = proveedorRepository.findById(producto.getIdProveedor());
        if (clienteOptional.isEmpty()) {
            return new CrudResponse("99", "Proveedor no encontrado");
        }

        productoRepository.save(producto);
        return new CrudResponse("01", "Producto actualizado.");

    }

    // Eliminar Producto
    @Transactional
    @PostMapping("/producto/delete")
    public CrudResponse deleteProducto(@RequestParam(value = "id", defaultValue = "0") Integer id){

        if(productoRepository.findById(id).isEmpty()){
            return new CrudResponse("99", "Producto no encontrado.");
        }

        // Traer la lista de detalles de venta por el id del producto
        List<DetalleVenta> detalleVentas = detalleVentaRepository.findAllByIdProducto(id);

        // Si la lista de detalles no es nula
        if(detalleVentas != null){
            // Eliminar los detalles de venta asociados al producto
            for(DetalleVenta detalleVenta : detalleVentas){
                detalleVentaRepository.deleteById(detalleVenta.getIdDetalle());
            }
        }

        // Traer las ventas que no tienen detalles de venta asociados
        List<Venta> ventas = ventaRepository.findVentasSinDetalles();

        // Eliminar las ventas que no tienen detalles de venta asociados
        for(Venta venta : ventas){
            ventaRepository.deleteById(venta.getIdVenta());
        }

        // Eliminar el producto
        productoRepository.deleteById(id);

        return new CrudResponse("01", "Producto eliminado.");

    }

}
