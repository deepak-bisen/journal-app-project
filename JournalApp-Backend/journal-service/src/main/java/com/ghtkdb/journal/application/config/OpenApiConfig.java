package com.ghtkdb.journal.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI journalOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Journal App API")
                        .version("1.0")
                        .description("REST API for Journal Application")
                        .contact(new Contact()
                                .name("Deepak Bisen")
                                .email("bisen1412@gmail.com")));
    }
}
