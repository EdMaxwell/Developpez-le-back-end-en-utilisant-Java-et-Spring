package com.openclassrooms.chatop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    /**
     * Configure la chaîne de filtres de sécurité pour l'application.
     *
     * - Désactive la protection CSRF (adapté pour les API REST stateless).
     * - Autorise l'accès sans authentification aux endpoints sous /api/auth/**.
     * - Exige l'authentification pour toutes les autres requêtes.
     * - Définit la gestion de session en mode STATELESS (aucune session côté serveur).
     * - Ajoute le filtre d'authentification JWT avant le filtre UsernamePasswordAuthenticationFilter.
     *
     * @param http l'objet HttpSecurity à configurer
     * @return la SecurityFilterChain configurée
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers("/api/rentals/*/picture").permitAll() // <-- accès public à l'image
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configure la source de configuration CORS pour l'application.
     *
     * - Définit les origines autorisées pour les requêtes CORS.
     * - Spécifie les méthodes HTTP autorisées (GET, POST).
     * - Spécifie les en-têtes autorisés (Authorization, Content-Type).
     * - Enregistre la configuration CORS pour tous les chemins (/**).
     *
     * @return une instance de CorsConfigurationSource configurée
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Définit les origines autorisées pour les requêtes CORS
        configuration.setAllowedOrigins(List.of("http://localhost:8005"));

        // Définit les méthodes HTTP autorisées
        configuration.setAllowedMethods(List.of("GET", "POST"));

        // Définit les en-têtes autorisés
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Crée une source de configuration basée sur les URL
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Enregistre la configuration CORS pour tous les chemins
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}