package com.farmacia.org.ProySpringFarmacia.response;

import com.farmacia.org.ProySpringFarmacia.entity.Cliente;

public record FindClienteResponse(String code, String mensaje, Iterable<Cliente> listaclientes) {
}
