package com.ms_pedidos.pedidos.repository;

import com.ms_pedidos.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Para que el usuario vea SU historial
    List<Pedido> findByUsuarioEmail(String usuarioEmail);
}