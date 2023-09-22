package com.ffreaky.paymentservice;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI finishFoodOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Finish Food Server (%s)".formatted(applicationName))
                        .description("API for Finish Food Application")
                        .version("1.0"));
    }
}
