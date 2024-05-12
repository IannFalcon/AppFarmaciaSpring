package com.farmacia.org.ProySpringFarmacia.response;

import com.farmacia.org.ProySpringFarmacia.entity.Empleado;

public record FindEmpleadoResponse(String code, String mensaje, Iterable<Empleado> listaempleados) {
}
