package com.ms_productos.productos.service;

import com.ms_productos.productos.model.Categoria;
import com.ms_productos.productos.model.Producto;
import com.ms_productos.productos.repository.CategoriaRepository;
import com.ms_productos.productos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // 1. Corregido: Método listarTodos() que faltaba
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // 2. Corregido: guardarProducto ahora recibe 'Producto' (no DTO)
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // 3. Corregido: Método obtenerProductoPorId() que faltaba
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    // 4. Corregido: Método para listar categorías
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }
    
    // Método para eliminar (por si lo necesitas a futuro)
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    // MÉTODO NUEVO PARA ACTUALIZAR
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto productoExistente = obtenerProductoPorId(id);
        
        if (productoExistente != null) {
            productoExistente.setNombre(productoActualizado.getNombre());
            productoExistente.setDescripcion(productoActualizado.getDescripcion());
            productoExistente.setPrecio(productoActualizado.getPrecio());
            productoExistente.setStock(productoActualizado.getStock());
            productoExistente.setImagenUrl(productoActualizado.getImagenUrl());
            productoExistente.setCategoria(productoActualizado.getCategoria());
            
            return productoRepository.save(productoExistente);
        }
        return null;
    }

    public boolean borrarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true; // Se borró con éxito
        }
        return false; // No existía el ID
    }

}