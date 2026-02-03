package com.ms_pedidos.pedidos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Pedidos",
                version = "v1",
                description = "Microservicio de pedidos: creación de órdenes y consulta de historial/estados."
        )
)
public class OpenApiConfig {
}
