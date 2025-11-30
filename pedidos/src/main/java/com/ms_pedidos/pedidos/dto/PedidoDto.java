package com.ms_pedidos.pedidos.dto;

import java.util.List;

public class PedidoDto {
    // IMPORTANTE: Debe llamarse "items" para coincidir con el JSON de React
    private List<DetalleDto> items;

    public List<DetalleDto> getItems() {
        return items;
    }

    public void setItems(List<DetalleDto> items) {
        this.items = items;
    }
}