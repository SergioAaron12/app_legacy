package com.ms_auth.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Auth",
                version = "v1",
                description = "Microservicio de autenticación: registro, login, validación de token y gestión de perfil."
        )
)
public class OpenApiConfig {
}
