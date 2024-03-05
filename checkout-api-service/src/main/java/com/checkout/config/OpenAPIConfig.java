package com.checkout.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        var contact = new Contact();
        contact.setEmail("rmangesh1@gmail.com");
        contact.setName("Mangesh");

        var info = new Info()
                .title("Checkout API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for checkout.");

        return new OpenAPI().info(info);
    }
}
