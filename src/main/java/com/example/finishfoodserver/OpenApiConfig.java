package com.example.finishfoodserver;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI finishFoodOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Finish Food API")
                        .description("API for Finish Food Application")
                        .version("1.0"));
    }
}
