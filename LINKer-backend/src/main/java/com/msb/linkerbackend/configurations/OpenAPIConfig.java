package com.msb.linkerbackend.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI defineOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development server");

        Contact contact = new Contact();
        contact.setName("Manobal Singh Bagady");
        contact.setUrl("https://www.linkedin.com/in/manobal-singh-bagady/");

        Info info = new Info()
                .title("LINKer API Documentation")
                .version("1.0")
                .description("A URL shortener and it's analytics project made in React and Spring Boot.")
                .contact(contact);

        return new OpenAPI().info(info).servers(List.of(server));
    }
}
