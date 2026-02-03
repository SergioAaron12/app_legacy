package com.ms_pedidos.pedidos.service;

import com.ms_pedidos.pedidos.dto.DetalleDto;
import com.ms_pedidos.pedidos.dto.PedidoDto;
import com.ms_pedidos.pedidos.model.DetallePedido;
import com.ms_pedidos.pedidos.model.Pedido;
import com.ms_pedidos.pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido crearPedido(String email, PedidoDto dto) {
        
        // 1. Crear el Pedido Padre
        Pedido pedido = new Pedido();
        pedido.setUsuarioEmail(email);
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setEstado("CONFIRMADO");

        // 2. Convertir los items del DTO a Entidades
        List<DetallePedido> detallesEntidad = new ArrayList<>();
        double totalCalculado = 0.0;

        if (dto.getItems() != null) {
            for (DetalleDto itemDto : dto.getItems()) {
                DetallePedido detalle = new DetallePedido();
                detalle.setProductoId(itemDto.getProductoId());
                detalle.setNombreProducto(itemDto.getNombreProducto());
                detalle.setCantidad(itemDto.getCantidad());
                detalle.setPrecioUnitario(itemDto.getPrecioUnitario());
                
                // VITAL: Vincular el hijo con el padre para que se guarde la Foreign Key
                detalle.setPedido(pedido);
                
                detallesEntidad.add(detalle);
                
                // Sumar al total
                totalCalculado += (itemDto.getPrecioUnitario() * itemDto.getCantidad());
            }
        }

        // 3. Asignar detalles y total al pedido
        pedido.setDetalles(detallesEntidad);
        pedido.setTotal(totalCalculado);

        // 4. Guardar en Base de Datos (El CascadeType.ALL guardará los detalles automáticamente)
        return pedidoRepository.save(pedido);
    }
}