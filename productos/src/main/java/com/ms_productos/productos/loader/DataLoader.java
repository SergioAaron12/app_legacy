package com.ms_productos.productos.loader;

import com.ms_productos.productos.model.Categoria;
import com.ms_productos.productos.model.Producto;
import com.ms_productos.productos.repository.CategoriaRepository;
import com.ms_productos.productos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        cargarDatos();
    }

    private void cargarDatos() {
        // Solo cargamos si la base de datos está vacía
        if (categoriaRepository.count() == 0) {
            
            System.out.println(">>> CARGANDO INVENTARIO ANTIGUO (LEGACY FRAMES) ... <<<");

            // --- 1. CREAR CATEGORÍAS (Usando Setters para evitar errores) ---
            
            Categoria catCuadros = new Categoria();
            catCuadros.setNombre("cuadros");
            catCuadros.setDescripcion("Marcos listos para colgar");

            Categoria catGrecas = new Categoria();
            catGrecas.setNombre("grecas");
            catGrecas.setDescripcion("Diseños clásicos y elegantes");

            Categoria catRusticas = new Categoria();
            catRusticas.setNombre("rusticas");
            catRusticas.setDescripcion("Estilo madera natural y envejecida");

            Categoria catNativas = new Categoria();
            catNativas.setNombre("nativas");
            catNativas.setDescripcion("Maderas nobles chilenas");

            Categoria catFinger = new Categoria();
            catFinger.setNombre("finger-joint");
            catFinger.setDescripcion("Uniones visibles, estilo moderno");

            Categoria catNaturales = new Categoria();
            catNaturales.setNombre("naturales");
            catNaturales.setDescripcion("Acabados suaves y minimalistas");

            // Guardamos las categorías primero
            categoriaRepository.saveAll(Arrays.asList(catCuadros, catGrecas, catRusticas, catNativas, catFinger, catNaturales));

            // --- 2. CREAR PRODUCTOS (MOLDURAS) ---
            
            // Grecas
            crearProd("I 09 greca zo", "Elegante greca decorativa con diseño tradicional ZO. Ideal para marcos clásicos.", 12500, 50, "/assets/mordura1.png", catGrecas);
            crearProd("I 09 greca corazón", "Hermosa greca con motivo de corazón, perfecta para marcos románticos.", 14000, 30, "/assets/mordura2.png", catGrecas);
            crearProd("P 15 greca LA oro", "Greca con acabado dorado, elegante y sofisticada.", 38000, 20, "/assets/moldura3.jpg", catGrecas);
            crearProd("P 15 greca LA plata", "Greca con acabado plateado, moderna y elegante.", 105000, 35, "/assets/moldura4.jpg", catGrecas);

            // Rústicas
            crearProd("H 20 albayalde azul", "Moldura rústica con acabado albayalde azul, ideal para ambientes campestres.", 130000, 45, "/assets/rustica1.jpg", catRusticas);

            // Nativas
            crearProd("J-16", "Moldura de madera nativa chilena, resistente y de gran calidad.", 73000, 15, "/assets/nativas1.jpg", catNativas);

            // Naturales y Modernas
            crearProd("B-10 t/alerce", "Moldura natural de alerce con textura original de la madera.", 6500, 100, "/assets/naturales1.jpg", catNaturales);

            // Finger Joint
            crearProd("P-12 Finger Joint", "Moldura finger joint de alta calidad con unión invisible.", 47000, 120, "/assets/finger_joint1.jpg", catFinger);


            // --- 3. CREAR PRODUCTOS (CUADROS LISTOS) ---

             crearProd("Marco Dorado Clásico", "Elegancia pura con acabado pan de oro envejecido.", 32500, 15, "/assets/marcoDoradoClasico.png", catCuadros);
            
            crearProd("Marco Minimalista Moderno", "Diseño contemporáneo y limpio, perfecto para espacios modernos y fotografías actuales.", 18990, 30, "/assets/marco-minimalista-ambiente-moderno_23-2149301885.jpg", catCuadros);
            
            crearProd("Marco Rústico de Madera", "Acabado rústico natural, ideal para ambientes campestres y fotografías de naturaleza.", 24990, 10, "/assets/marcoRustico.png", catCuadros);
            
            crearProd("Marco para Diplomas", "Especializado en enmarcación de diplomas y certificados importantes con protección UV.", 15000, 40, "/assets/marco.diplomaa.png", catCuadros);
            
            crearProd("Marco Vintage Antiguo", "Estilo vintage con detalles ornamentales, perfecto para fotografías familiares clásicas.", 55000, 5, "/assets/marcoantigo.png", catCuadros);

            crearProd("Marco para Camisetas", "Especializado en enmarcación de camisetas deportivas y memorabilia con montaje especial.", 20000, 10, "/assets/polera.1.png", catCuadros);

            System.out.println(">>> ¡INVENTARIO CARGADO CON ÉXITO! <<<");
        } else {
            System.out.println(">>> La base de datos ya tiene productos. No se cargaron duplicados. <<<");
        }
    }

    // Método auxiliar para crear productos (Usa setters para ser compatible con cualquier versión de tu modelo)
    private void crearProd(String nombre, String descripcion, double precio, int stock, String imagen, Categoria categoria) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setImagenUrl(imagen);
        p.setCategoria(categoria);
        productoRepository.save(p);
    }
}