package com.ms_productos.productos.controller;

import com.ms_productos.productos.model.Categoria;
import com.ms_productos.productos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/categorias") // <--- Esta es la ruta que daba 404
@CrossOrigin(origins = "http://localhost:5173") // <--- Permiso para el Frontend
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Listar todas las categorías
    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }
}