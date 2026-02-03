package com.ms_contacto.contacto.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Contacto",
                version = "v1",
                description = "Microservicio de contactos: alta, baja, modificación y consulta de contactos."
        )
)
public class OpenApiConfig {
}
