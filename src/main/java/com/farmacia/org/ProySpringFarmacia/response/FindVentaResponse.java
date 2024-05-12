package com.farmacia.org.ProySpringFarmacia.response;

import com.farmacia.org.ProySpringFarmacia.entity.Venta;

public record FindVentaResponse(String code, String mensaje, Iterable<Venta> ventas) {
}
