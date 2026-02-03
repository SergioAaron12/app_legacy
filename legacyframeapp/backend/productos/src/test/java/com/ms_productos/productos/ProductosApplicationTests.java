package com.ms_productos.productos;

import com.ms_productos.productos.repository.CategoriaRepository;
import com.ms_productos.productos.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductosApplicationTests {

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Test
	void contextLoads() {
		// Verifica que el contexto de Spring Boot se carga correctamente
		assertNotNull(productoRepository);
		assertNotNull(categoriaRepository);
	}

	@Test
	void verificarInventarioCargado() {
		// Verifica que el inventario inicial se cargó (DataLoader)
		long cantidadProductos = productoRepository.count();
		long cantidadCategorias = categoriaRepository.count();

		assertTrue(cantidadProductos > 0, "Debe haber productos cargados");
		assertTrue(cantidadCategorias > 0, "Debe haber categorías cargadas");
	}

	@Test
	void verificarProductoPorId() {
		// Verifica que se puede buscar un producto por ID
		var producto = productoRepository.findById(1L);
		assertTrue(producto.isPresent(), "El producto con ID 1 debe existir");
		assertNotNull(producto.get().getNombre(), "El producto debe tener un nombre");
	}

}
