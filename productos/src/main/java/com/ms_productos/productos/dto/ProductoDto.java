package com.ms_productos.productos.dto;
import lombok.Data;

@Data
public class ProductoDto {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String imagenUrl;
    private Long categoriaId; // Recibimos solo el ID
}