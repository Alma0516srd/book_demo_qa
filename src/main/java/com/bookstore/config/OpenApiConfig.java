package com.bookstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Order System API")
                        .version("1.0.0")
                        .description("API for book ordering system. Intentionally contains bugs for tester training.")
                        .contact(new Contact()
                                .name("BookStore Team")
                                .email("support@bookstore.com")));
    }
}
