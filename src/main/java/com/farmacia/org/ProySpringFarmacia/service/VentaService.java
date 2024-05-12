package com.farmacia.org.ProySpringFarmacia.service;

import com.farmacia.org.ProySpringFarmacia.entity.*;
import com.farmacia.org.ProySpringFarmacia.repository.*;
import com.farmacia.org.ProySpringFarmacia.response.CrudResponse;
import com.farmacia.org.ProySpringFarmacia.response.FindVentaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Farmacia")
public class VentaService {

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    DetalleVentaRepository detalleVentaRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    // Listar Ventas
    @GetMapping("/ventas/all")
    public FindVentaResponse findVenta(@RequestParam(value = "id", defaultValue = "0") Integer id) {

        Iterable<Venta> ventas = null;

        if (id > 0) {
            ventas = ventaRepository.findAllById(List.of(id));
        } else {
            ventas = ventaRepository.findAll();
        }

        return new FindVentaResponse("01", "Ventas encontradas.", ventas);

    }

    // Nueva Venta
    @PostMapping("/ventas/add")
    public CrudResponse addVenta(@RequestBody NuevaVenta nuevaVenta) {

        // Verificar si la venta existe
        Optional<Venta> ventaOptional = ventaRepository.findById(nuevaVenta.getIdNuevaVenta());

        // Si no existe se crea la venta y su detalle
        if (ventaOptional.isEmpty()) {

            // Verificar si el producto existe
            Optional<Producto> productoOptional = productoRepository.findById(nuevaVenta.getIdProducto());
            if (productoOptional.isEmpty()) {
                return new CrudResponse("99", "Producto no encontrado");
            }

            // Verificar si el empleado existe
            Optional<Empleado> empleadoOptional = empleadoRepository.findById(nuevaVenta.getIdEmpleado());
            if (empleadoOptional.isEmpty()) {
                return new CrudResponse("99", "Empleado no encontrado");
            }

            // Verificar si el cliente existe
            Optional<Cliente> clienteOptional = clienteRepository.findById(nuevaVenta.getIdCliente());
            if (clienteOptional.isEmpty()) {
                return new CrudResponse("99", "Cliente no encontrado");
            }

            // Calcular el precio unitario del producto
            Producto producto = productoOptional.get();
            Double precioUnitario = producto.getPrecio();

            // Calcular el monto total
            Double montoTotal = nuevaVenta.getCantidad() * precioUnitario;

            // Crear la nueva venta
            Venta venta = new Venta();
            venta.setIdEmpleado(nuevaVenta.getIdEmpleado());
            venta.setIdCliente(nuevaVenta.getIdCliente());
            venta.setFechaVenta(new Date());
            venta.setImporteTotal(montoTotal);

            // Guardar la venta
            ventaRepository.save(venta);

            // Crear el nuevo detalle de venta
            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setIdVenta(venta.getIdVenta());
            detalleVenta.setIdProducto(nuevaVenta.getIdProducto());
            detalleVenta.setCantidad(nuevaVenta.getCantidad());
            detalleVenta.setPrecioUnitario(precioUnitario);

            // Guardar el detalle de venta
            detalleVentaRepository.save(detalleVenta);

            // Actualizar el stock del producto
            producto.setStock(producto.getStock() - nuevaVenta.getCantidad());
            productoRepository.save(producto);

            return new CrudResponse("01", "Venta generada con exito.");

        } else {

            // Si existe agregamos un detalle de venta y actulizamos la venta
            // Traemos la venta
            Venta venta = ventaOptional.get();

            // Verificar si el producto existe
            Optional<Producto> productoOptional = productoRepository.findById(nuevaVenta.getIdProducto());
            if (productoOptional.isEmpty()) {
                return new CrudResponse("99", "Producto no encontrado");
            }

            // Verificar si el empleado existe
            Optional<Empleado> empleadoOptional = empleadoRepository.findById(nuevaVenta.getIdEmpleado());
            if (empleadoOptional.isEmpty()) {
                return new CrudResponse("99", "Empleado no encontrado");
            }

            // Verificar si el cliente existe
            Optional<Cliente> clienteOptional = clienteRepository.findById(nuevaVenta.getIdCliente());
            if (clienteOptional.isEmpty()) {
                return new CrudResponse("99", "Cliente no encontrado");
            }

            // Calcular el precio unitario del producto
            Producto producto = productoOptional.get();
            Double precioUnitario = producto.getPrecio();

            // Calcular el monto total
            Double montoTotal = nuevaVenta.getCantidad() * precioUnitario;

            // Actualizamos el importe
            venta.setImporteTotal(montoTotal);

            ventaRepository.save(venta);

            // Nuevo detalle de venta
            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setIdVenta(venta.getIdVenta());
            detalleVenta.setIdProducto(nuevaVenta.getIdProducto());
            detalleVenta.setCantidad(nuevaVenta.getCantidad());
            detalleVenta.setPrecioUnitario(precioUnitario);

            // Guardar el detalle de venta
            detalleVentaRepository.save(detalleVenta);

            // Actualizar el stock del producto
            producto.setStock(producto.getStock() - nuevaVenta.getCantidad());
            productoRepository.save(producto);

            return new CrudResponse("01", "Detalle de venta agregado a la venta con codigo: " + venta.getIdVenta());

        }

    }

    // Actualizar Venta
    @PostMapping("/ventas/update")
    public CrudResponse updateVenta(@RequestBody NuevaVenta nuevaVenta) {

        Optional<Venta> ventaOptional = ventaRepository.findById(nuevaVenta.getIdNuevaVenta());
        if (ventaOptional.isEmpty()) {
            return new CrudResponse("99", "Venta no encontrada");
        }

        // Verificar si el empleado existe
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(nuevaVenta.getIdEmpleado());
        if (empleadoOptional.isEmpty()) {
            return new CrudResponse("99", "Empleado no encontrado");
        }

        // Verificar si el cliente existe
        Optional<Cliente> clienteOptional = clienteRepository.findById(nuevaVenta.getIdCliente());
        if (clienteOptional.isEmpty()) {
            return new CrudResponse("99", "Cliente no encontrado");
        }

        Venta venta = ventaOptional.get();
        venta.setIdEmpleado(nuevaVenta.getIdEmpleado());
        venta.setIdCliente(nuevaVenta.getIdCliente());
        venta.setFechaVenta(new Date());

        ventaRepository.save(venta);

        return new CrudResponse("01", "Venta actualizada.");

    }

    // Eliminar Venta
    @Transactional
    @PostMapping("/ventas/delete")
    public CrudResponse deleteVenta(@RequestParam(value = "id", defaultValue = "0") Integer id) {

        if (ventaRepository.findById(id).isEmpty()) {
            return new CrudResponse("99", "Venta no encontrada.");
        }

        // Traer la lista de detalles de venta
        List<DetalleVenta> detalleVentas = detalleVentaRepository.findAllByIdVenta(id);

        // Si la lista de detalles no es nula
        if (detalleVentas != null) {
            // Eliminar los detalles de venta asociados a la venta
            for (DetalleVenta detalleVenta : detalleVentas) {
                detalleVentaRepository.deleteById(detalleVenta.getIdDetalle());
            }
        }

        // Eliminar la venta
        ventaRepository.deleteById(id);

        return new CrudResponse("01", "Venta eliminada.");

    }

}
