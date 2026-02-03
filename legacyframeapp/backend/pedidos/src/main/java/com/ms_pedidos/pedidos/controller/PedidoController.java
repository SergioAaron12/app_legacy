package com.ms_pedidos.pedidos.controller;

import com.ms_pedidos.pedidos.model.Pedido;
import com.ms_pedidos.pedidos.repository.PedidoRepository;
import com.ms_pedidos.pedidos.service.PedidoService;
import com.ms_pedidos.pedidos.dto.PedidoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173") 
@Tag(name = "Pedidos", description = "Creación y consulta de pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    // Crear pedido
    @PostMapping
        @Operation(
            summary = "Crear pedido",
            description = "Crea un pedido para el usuario indicado por email usando el detalle enviado en el body."
        )
    // OJO AQUÍ: Aseguramos que reciba el Body (JSON) y el Param (Email)
        public ResponseEntity<Pedido> crearPedido(
            @RequestBody PedidoDto pedidoDto,
            @Parameter(description = "Email del usuario dueño del pedido")
            @RequestParam String email
        ) {
        
        // IMPORTANTE: Aquí llamamos al servicio. 
        // Si te da error en rojo aquí, es porque en el Service están al revés.
        // Intenta con (email, pedidoDto) o (pedidoDto, email) según lo que pida tu Service.
        
        // Opción A (La que corregimos ayer):
        Pedido nuevoPedido = pedidoService.crearPedido(email, pedidoDto); 
        
        return ResponseEntity.ok(nuevoPedido);
    }

    // Listar (Admin)
    @GetMapping("/admin/all")
    @Operation(
            summary = "Listar todos los pedidos (admin)",
            description = "Devuelve todos los pedidos existentes (uso administrativo)."
    )
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoRepository.findAll());
    }

    // Historial del cliente
    @GetMapping("/my-orders")
    @Operation(
            summary = "Historial de pedidos por email",
            description = "Devuelve el historial de pedidos del usuario indicado por email."
    )
    public ResponseEntity<List<Pedido>> misPedidos(
            @Parameter(description = "Email del usuario")
            @RequestParam String email
    ) {
        List<Pedido> historial = pedidoRepository.findByUsuarioEmail(email);
        return ResponseEntity.ok(historial);
    }
}