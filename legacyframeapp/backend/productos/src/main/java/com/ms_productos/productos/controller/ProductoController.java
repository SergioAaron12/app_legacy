package com.ms_productos.productos.controller;

import com.ms_productos.productos.model.Producto;
import com.ms_productos.productos.service.ProductoService;
import com.ms_productos.productos.repository.ProductoRepository; // Si lo usas para listar
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/productos")
// AQUÍ ESTÁ EL ARREGLO: Permitimos explícitamente DELETE y PUT
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Tag(name = "Productos", description = "Gestión de catálogo de productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    // Listar todos
    @GetMapping
        @Operation(
            summary = "Listar productos",
            description = "Devuelve todos los productos del catálogo."
        )
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    // Obtener por ID
    @GetMapping("/{id}")
        @Operation(
            summary = "Obtener producto por id",
            description = "Devuelve un producto específico por su identificador."
        )
        public ResponseEntity<Producto> obtenerProducto(
            @Parameter(description = "ID del producto")
            @PathVariable Long id
        ) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear
    @PostMapping
        @Operation(
            summary = "Crear producto",
            description = "Crea un producto nuevo en el catálogo."
        )
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    // Editar
    @PutMapping("/{id}")
        @Operation(
            summary = "Actualizar producto",
            description = "Actualiza los datos de un producto existente por id."
        )
        public ResponseEntity<Producto> actualizarProducto(
            @Parameter(description = "ID del producto")
            @PathVariable Long id,
            @RequestBody Producto detalles
        ) {
        return productoRepository.findById(id)
                .map(prod -> {
                    prod.setNombre(detalles.getNombre());
                    prod.setDescripcion(detalles.getDescripcion());
                    prod.setPrecio(detalles.getPrecio());
                    prod.setStock(detalles.getStock());
                    prod.setImagenUrl(detalles.getImagenUrl());
                    prod.setCategoria(detalles.getCategoria());
                    return ResponseEntity.ok(productoRepository.save(prod));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // --- BORRAR (El que daba error 405) ---
    @DeleteMapping("/{id}")
        @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto del catálogo por id."
        )
        public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "ID del producto")
            @PathVariable Long id
        ) {
        // Usamos el servicio que creamos antes
        boolean eliminado = productoService.borrarProducto(id);
        
        if (eliminado) {
            return ResponseEntity.noContent().build(); // 204 No Content (Éxito)
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}