package com.farmacia.org.ProySpringFarmacia.response;

import com.farmacia.org.ProySpringFarmacia.entity.Producto;

public record FindProductoResponse(String code, String mensaje, Iterable<Producto> listaproductos) {
}
