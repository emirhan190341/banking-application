package com.emirhanarici.bankapplication.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration class for OpenAPI documentation of the Simple Banking App API.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates and configures the OpenAPI documentation for the Simple Banking App API.
     *
     * @return An OpenAPI instance containing API information.
     */
    @Bean
    public OpenAPI bankingApiOpenAPI() {
        Contact contact = new Contact()
                .name("Emirhan Arici")
                .url("https://github.com/emirhan190341/banking-application");

        Info info = new Info()
                .title("Simple Banking App API")
                .version("1.0.0")
                .description("Simple Banking App " +
                        "(Spring Boot, Maven, JUnit, Integration Test, Mysql, Prometheus, Grafana, Github Actions, Postman)")
                .contact(contact);

        return new OpenAPI().info(info);
    }


}
