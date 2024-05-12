package com.farmacia.org.ProySpringFarmacia.response;

import com.farmacia.org.ProySpringFarmacia.entity.Proveedor;

public record FindProveedorResponse(String code, String mensaje, Iterable<Proveedor> proveedores) {
}
