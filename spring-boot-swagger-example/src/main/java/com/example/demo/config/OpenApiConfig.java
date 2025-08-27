package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Spring Boot Swagger Example API",
        version = "v1.0.0",
        description = "This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.0",
        termsOfService = "http://swagger.io/terms/",
        contact = @Contact(
            name = "API Support",
            url = "http://www.example.com/support",
            email = "support@example.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"
        )
    ),
    servers = {
        @Server(
            description = "Local ENV",
            url = "http://localhost:8080"
        ),
        @Server(
            description = "Development ENV", 
            url = "http://dev.example.com"
        ),
        @Server(
            description = "Production ENV",
            url = "https://api.example.com"
        )
    }
)
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI(@Value("${application.version:1.0.0}") String appVersion) {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT Bearer Token")
                )
            )
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("Spring Boot Swagger Example API")
                .version(appVersion)
                .description("This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.0")
                .termsOfService("http://swagger.io/terms/")
                .contact(new io.swagger.v3.oas.models.info.Contact()
                    .name("API Support")
                    .email("support@example.com")
                    .url("http://www.example.com/support"))
                .license(new io.swagger.v3.oas.models.info.License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
            );
    }
}