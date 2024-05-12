package com.farmacia.org.ProySpringFarmacia.response;

import com.farmacia.org.ProySpringFarmacia.entity.DetalleVenta;

public record FindDetalleVentaResponse(String code, String mensaje, Iterable<DetalleVenta> detalleVentas) {
}
