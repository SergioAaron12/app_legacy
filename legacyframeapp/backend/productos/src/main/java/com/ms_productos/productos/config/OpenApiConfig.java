package com.ms_productos.productos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Productos",
                version = "v1",
                description = "Microservicio de catálogo: productos, categorías, stock y precios."
        )
)
public class OpenApiConfig {
}
