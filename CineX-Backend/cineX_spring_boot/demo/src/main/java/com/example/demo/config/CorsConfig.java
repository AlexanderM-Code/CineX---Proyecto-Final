package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Permite que el frontend (otro dominio o puerto) llame a la API.
 * Los orígenes se configuran con la variable CORS_ORIGINS (por defecto, todos).
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cinex.cors.allowed-origins:*}")
    private String[] origenesPermitidos;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(origenesPermitidos)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
