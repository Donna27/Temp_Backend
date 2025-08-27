package com.kbtg.tempbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Temp Backend API")
                        .version("1.0.0")
                        .description("A simple Spring Boot API with Hello World endpoint")
                        .contact(new Contact()
                                .name("KBTG")
                                .email("support@kbtg.com")));
    }
}
