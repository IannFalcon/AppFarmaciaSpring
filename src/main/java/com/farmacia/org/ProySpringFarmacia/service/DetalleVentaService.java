package com.farmacia.org.ProySpringFarmacia.service;

import com.farmacia.org.ProySpringFarmacia.entity.*;
import com.farmacia.org.ProySpringFarmacia.repository.DetalleVentaRepository;
import com.farmacia.org.ProySpringFarmacia.repository.ProductoRepository;
import com.farmacia.org.ProySpringFarmacia.repository.VentaRepository;
import com.farmacia.org.ProySpringFarmacia.response.CrudResponse;
import com.farmacia.org.ProySpringFarmacia.response.FindDetalleVentaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Farmacia")
public class DetalleVentaService {

    // Inyeccion de dependencias
    @Autowired
    DetalleVentaRepository detalleVentaRepository;

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    ProductoRepository productoRepository;

    @GetMapping("/detalleVenta/all")
    public FindDetalleVentaResponse findDetalleVenta(@RequestParam(value = "id", defaultValue = "0") Integer id) {

        Iterable<DetalleVenta> detalleVentas = null;

        if (id > 0) {
            detalleVentas = detalleVentaRepository.findAllById(List.of(id));
        } else {
            detalleVentas = detalleVentaRepository.findAll();
        }

        return new FindDetalleVentaResponse("01", "Detalles de venta encontrados", detalleVentas);

    }

    @PostMapping("/detalleVenta/update")
    public CrudResponse updateDetalleVenta(@RequestBody DetalleVenta detalleVenta) {

        // Buscar el detalle de venta a actualizar por su id
        Optional<DetalleVenta> detalleVentaOptional = detalleVentaRepository.findById(detalleVenta.getIdDetalle());

        if (detalleVentaOptional.isEmpty()) {

            return new CrudResponse("99", "Error, Detalle de venta no encontrado.");

        } else {

            // Verificar si el producto existe
            Optional<Producto> productoOptional = productoRepository.findById(detalleVenta.getIdProducto());
            if (productoOptional.isEmpty()) {
                return new CrudResponse("99", "Error, Producto no encontrado.");
            }

            // Verificar si la venta existe
            Optional<Venta> ventaOptional = ventaRepository.findById(detalleVenta.getIdVenta());
            if (ventaOptional.isEmpty()) {
                return new CrudResponse("99", "Error, Venta no encontrado.");
            }

            // Calcular el precio unitario del producto
            Producto producto = productoOptional.get();
            Double precioUnitario = producto.getPrecio();

            // Calcular el monto total de la venta
            Double montoTotal = detalleVenta.getCantidad() * precioUnitario;

            // Actualizar los campos del detalle de venta con los valores del detalle de venta actualizado
            DetalleVenta actualizado = detalleVentaOptional.get();
            actualizado.setIdVenta(detalleVenta.getIdVenta());
            actualizado.setIdProducto(producto.getIdProducto());
            actualizado.setCantidad(detalleVenta.getCantidad());
            actualizado.setPrecioUnitario(producto.getPrecio());

            // Obtenemos la venta
            Venta venta = ventaOptional.get();

            // Actualizamos el importe
            venta.setImporteTotal(montoTotal);
            ventaRepository.save(venta);

            // Actualizar el stock del producto
            producto.setStock(producto.getStock() - detalleVenta.getCantidad());
            productoRepository.save(producto);

            // Guardar el detalle de venta actualizado
            detalleVentaRepository.save(actualizado);

            return new CrudResponse("01", "Detalle de Venta actualizado correctamente.");

        }

    }

    @PostMapping("/detalleVenta/delete")
    public CrudResponse deleteDetalleVenta(@RequestParam(value = "id", defaultValue = "0") Integer id){

        if(detalleVentaRepository.findById(id).isEmpty()){
            return new CrudResponse("99", "Error, Detalle de Venta no encontrado.");
        }

        // Traer las ventas que no tienen detalles de venta asociados
        List<Venta> ventas = ventaRepository.findVentasSinDetalles();

        // Eliminar las ventas que no tienen detalles de venta asociados
        for(Venta venta : ventas){
            ventaRepository.deleteById(venta.getIdVenta());
        }

        detalleVentaRepository.deleteById(id);
        return new CrudResponse("01", "Detalle de venta eliminado.");

    }

}