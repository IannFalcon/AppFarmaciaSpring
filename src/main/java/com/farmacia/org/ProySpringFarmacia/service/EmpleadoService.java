package com.farmacia.org.ProySpringFarmacia.service;

import com.farmacia.org.ProySpringFarmacia.entity.Empleado;
import com.farmacia.org.ProySpringFarmacia.entity.Venta;
import com.farmacia.org.ProySpringFarmacia.repository.DetalleVentaRepository;
import com.farmacia.org.ProySpringFarmacia.repository.EmpleadoRepository;
import com.farmacia.org.ProySpringFarmacia.repository.VentaRepository;
import com.farmacia.org.ProySpringFarmacia.response.CrudResponse;
import com.farmacia.org.ProySpringFarmacia.response.FindEmpleadoResponse;
import com.farmacia.org.ProySpringFarmacia.response.LoginResponse;
import com.farmacia.org.ProySpringFarmacia.security.JWTAuthenticationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Farmacia")
public class EmpleadoService {

    // Inyeccion de dependecias
    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    DetalleVentaRepository detalleVentaRepository;

    @Autowired
    JWTAuthenticationConfig jwtAuthenticationConfig;

    // Login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody Empleado empleado){

        Empleado userResult = empleadoRepository.findByUsername(empleado.getUsername());
        if(userResult == null){
            return new LoginResponse("99", "Nombre de usuario incorrecto.", null);
        }
        if (new BCryptPasswordEncoder().matches(empleado.getPassword(), userResult.getPassword())){
            return new LoginResponse("99", "Contraseña incorrecta.", null);
        }

        String token = jwtAuthenticationConfig.getJWTToken(userResult.getUsername());
        return new LoginResponse("01", "Inicio de sesión exitoso.", token);

    }

    // Listar Empleados
    @GetMapping("/empleado/all")
    public FindEmpleadoResponse findEmpleado(@RequestParam(value = "id", defaultValue = "0") Integer id){

        Iterable<Empleado> empleados = null;

        if(id > 0){
            empleados = empleadoRepository.findAllById(List.of(id));
        } else {
            empleados = empleadoRepository.findAll();
        }

        return new FindEmpleadoResponse("01", "Empleados encontrados.", empleados);

    }

    // Listar Empleados Por Nombre
    @GetMapping("/empleado/nombre")
    public FindEmpleadoResponse findEmpleadoByNombre(@RequestParam(value = "nombre", defaultValue = "") String nombre){

        Iterable<Empleado> empleados = null;

        if(nombre != null && !nombre.isEmpty()){
            empleados = empleadoRepository.findAllByNombreStartingWith(nombre);
        } else {
            empleados = empleadoRepository.findAll();
        }

        return new FindEmpleadoResponse("01", "Empleados encontrados.", empleados);

    }

    // Registrar Empleado
    @PostMapping("/empleado/add")
    public CrudResponse addEmpleado(@RequestBody Empleado empleado){

        if(empleado.getIdEmpleado() != null){
            return new CrudResponse("99", "Parametro ID no permitido.");
        }

        empleadoRepository.save(empleado);
        return new CrudResponse("01", "Empleado registrado con exito.");

    }

    // Actualizar Empleado
    @PostMapping("/empleado/update")
    public CrudResponse updateEmpleado(@RequestBody Empleado empleado){

        if(empleadoRepository.findById(empleado.getIdEmpleado()).isEmpty()){
            return new CrudResponse("99", "Empleado no encontrado.");
        }

        empleadoRepository.save(empleado);
        return new CrudResponse("01", "Empleado actualizado.");

    }

    // Eliminar Empleado
    @Transactional
    @PostMapping("/empleado/delete")
    public CrudResponse deleteEmpleado(@RequestParam(value = "id", defaultValue = "0") Integer id){

        if(empleadoRepository.findById(id).isEmpty()){
            return new CrudResponse("99", "Empleado no encontrado.");
        }

        // Traer la lista de ventas por el ID del Empleado
        List<Venta> ventas = ventaRepository.findAllByIdEmpleado(id);

        // Si la lista de ventas no es nula
        if(ventas != null){
            // Eliminar los detalles de venta asociados a la venta
            for(Venta venta : ventas){
                detalleVentaRepository.deleteByIdVenta(venta.getIdVenta());
            }
        }

        // Eliminar las ventas asociadas al empleado
        ventaRepository.deleteByIdEmpleado(id);

        // Eliminar al empleado
        empleadoRepository.deleteById(id);

        return new CrudResponse("01", "Empleado eliminado con exito.");

    }

}
