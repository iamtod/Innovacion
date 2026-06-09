package com.sistema.solicitudes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger / OpenAPI para documentar la API de COMIC S.A.
 * Accesible en: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI comicSaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("COMIC S.A - API de Soporte Técnico")
                        .description("""
                                API RESTful para la gestión de solicitudes de soporte técnico de COMIC S.A.
                                
                                Permite registrar, consultar, actualizar y eliminar solicitudes de soporte,\s
                                así como gestionar clientes y técnicos del equipo.
                                
                                **Entidades principales:**
                                - **Solicitudes**: Tickets de soporte técnico (CRUD completo)
                                - **Clientes**: Empresas y personas que solicitan soporte
                                - **Técnicos**: Personal de soporte de COMIC S.A.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo - COMIC S.A.")
                                .email("dev@comicsa.pe"))
                        .license(new License()
                                .name("MIT License")));
    }
}
