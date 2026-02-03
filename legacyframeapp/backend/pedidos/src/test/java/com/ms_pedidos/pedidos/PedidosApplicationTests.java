package com.ms_pedidos.pedidos;

import com.ms_pedidos.pedidos.model.Pedido;
import com.ms_pedidos.pedidos.model.DetallePedido;
import com.ms_pedidos.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PedidosApplicationTests {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Test
	void contextLoads() {
		// Verifica que el contexto de Spring Boot se carga correctamente
		assertNotNull(pedidoRepository);
	}

	@Test
	void crearPedidoConDetalles() {
		// Crea un pedido con detalles y verifica que se persiste correctamente
		Pedido pedido = new Pedido();
		pedido.setUsuarioEmail("test@duoc.cl");
		pedido.setEstado("PENDIENTE");
		pedido.setFechaCreacion(LocalDateTime.now());
		pedido.setTotal(15000.0);

		DetallePedido detalle = new DetallePedido();
		detalle.setProductoId(1L);
		detalle.setNombreProducto("Marco Test");
		detalle.setCantidad(2);
		detalle.setPrecioUnitario(7500.0);
		detalle.setPedido(pedido);

		List<DetallePedido> detalles = new ArrayList<>();
		detalles.add(detalle);
		pedido.setDetalles(detalles);

		Pedido savedPedido = pedidoRepository.save(pedido);

		assertNotNull(savedPedido.getId());
		assertEquals(1, savedPedido.getDetalles().size());
		assertEquals("test@duoc.cl", savedPedido.getUsuarioEmail());
	}

	@Test
	void buscarPedidosPorUsuario() {
		// Crea un pedido y verifica que se puede buscar por email
		Pedido pedido = new Pedido();
		pedido.setUsuarioEmail("user@test.com");
		pedido.setEstado("COMPLETADO");
		pedido.setFechaCreacion(LocalDateTime.now());
		pedido.setTotal(5000.0);
		pedido.setDetalles(new ArrayList<>());

		pedidoRepository.save(pedido);

		List<Pedido> pedidos = pedidoRepository.findByUsuarioEmailOrderByFechaCreacionDesc("user@test.com");

		assertFalse(pedidos.isEmpty());
		assertEquals("user@test.com", pedidos.get(0).getUsuarioEmail());
	}

}
