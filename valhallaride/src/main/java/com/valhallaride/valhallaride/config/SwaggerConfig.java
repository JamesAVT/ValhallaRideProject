package com.valhallaride.valhallaride.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration // Anotación que indica que esta clase es una clase de configuración de Spring. 
              // Spring Boot escaneará esta clase al iniciar la aplicación y procesará los @Bean que tenga
public class SwaggerConfig {

    @Bean 
    public OpenAPI customAPI() {
        return new OpenAPI() // Se crea el objeto OpenAPI 
                .info(new Info()
                        .title("API administración de tienda ValhallaRide")
                        .version("1.0")
                        .description("Con esta API se puede administar la tienda de ValhallaRide"));
    }
}
