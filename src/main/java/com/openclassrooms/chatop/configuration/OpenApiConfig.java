package com.openclassrooms.chatop.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration OpenAPI/Swagger pour la documentation de l'API Chatop.
 * 
 * <p>Cette configuration génère automatiquement la documentation interactive
 * de l'API REST, incluant la gestion de l'authentification JWT Bearer.</p>
 * 
 * <p>Fonctionnalités configurées :</p>
 * <ul>
 *   <li>Documentation interactive via Swagger UI</li>
 *   <li>Schéma d'authentification JWT Bearer</li>
 *   <li>Spécification OpenAPI 3.0</li>
 *   <li>Métadonnées de l'API (titre, version, description)</li>
 * </ul>
 * 
 * <p>URLs générées :</p>
 * <ul>
 *   <li>Swagger UI : /swagger-ui/index.html</li>
 *   <li>Spécification JSON : /v3/api-docs</li>
 * </ul>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * Nom du schéma de sécurité JWT utilisé dans la documentation.
     */
    private static final String SECURITY_SCHEME_NAME = "bearer-jwt";

    /**
     * Configuration OpenAPI principale pour l'API Chatop.
     * 
     * <p>Cette méthode configure :</p>
     * <ul>
     *   <li>Les métadonnées de l'API (titre, version, description, contact)</li>
     *   <li>Le schéma d'authentification JWT Bearer</li>
     *   <li>L'application automatique de la sécurité sur tous les endpoints</li>
     * </ul>
     * 
     * <p>Pour utiliser un endpoint protégé via Swagger UI :</p>
     * <ol>
     *   <li>Authentifiez-vous via /api/auth/login</li>
     *   <li>Copiez le token JWT retourné</li>
     *   <li>Cliquez sur "Authorize" dans Swagger UI</li>
     *   <li>Entrez "Bearer {votre-token}" dans le champ Value</li>
     * </ol>
     * 
     * @return Configuration OpenAPI complète pour l'application Chatop
     */
    @Bean
    public OpenAPI chatopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Chatop API")
                        .version("v1")
                        .description("API REST pour la gestion de locations immobilières avec authentification JWT.")
                        .contact(new Contact()
                                .name("OpenClassrooms Support")
                                .url("https://openclassrooms.com")
                                .email("support@openclassrooms.com"))
                        .license(new License()
                                .name("Educational License")
                                .url("https://openclassrooms.com")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .schemaRequirement(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Bearer Token. Format: Bearer {your-jwt-token}"));
    }
}