package com.ms_productos.productos.controller;

import com.ms_productos.productos.model.Producto;
import com.ms_productos.productos.service.ProductoService;
import com.ms_productos.productos.repository.ProductoRepository; // Si lo usas para listar
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/productos")
// AQUÍ ESTÁ EL ARREGLO: Permitimos explícitamente DELETE y PUT
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    // Listar todos
    @GetMapping
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear
    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    // Editar
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto detalles) {
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
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        // Usamos el servicio que creamos antes
        boolean eliminado = productoService.borrarProducto(id);
        
        if (eliminado) {
            return ResponseEntity.noContent().build(); // 204 No Content (Éxito)
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}