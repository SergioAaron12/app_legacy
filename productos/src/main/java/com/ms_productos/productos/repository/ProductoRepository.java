package com.ms_productos.productos.repository;
import com.ms_productos.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Método extra útil: Buscar productos por ID de categoría
    List<Producto> findByCategoriaId(Long categoriaId);
}