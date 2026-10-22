package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Datos que muestra la documentación en /swagger-ui.html */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cinexOpenApi() {
        return new OpenAPI().info(new Info()
                .title("CineX API")
                .version("2.0")
                .description("Gestión de un cine: películas, salas, funciones, compra de sillas, "
                        + "productos de confitería, usuarios y reservas."));
    }
}
