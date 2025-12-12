package com.ms_productos.productos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    
    // --- CAMBIO IMPORTANTE: AUMENTAMOS EL TAMAÑO ---
    @Column(length = 5000) 
    private String imagenUrl; 

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}