package com.openclassrooms.chatop.configuration;

import com.openclassrooms.chatop.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Filtre d'authentification JWT pour chaque requête HTTP.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructeur du filtre JWT.
     *
     * @param jwtService service de gestion des tokens JWT
     * @param userDetailsService service de récupération des utilisateurs
     * @param handlerExceptionResolver gestionnaire des exceptions
     */
    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    /**
     * Filtre la requête pour vérifier et authentifier le token JWT.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Récupère l'en-tête Authorization
        final String authHeader = request.getHeader("Authorization");

        // Si l'en-tête est absent ou ne commence pas par "Bearer ", passe au filtre suivant
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }


        try {
            // Extrait le token JWT de l'en-tête
            final String jwt = authHeader.substring(7);
            // Extrait l'email de l'utilisateur à partir du token
            final String userEmail = jwtService.extractUsername(jwt);

            // Vérifie si l'utilisateur n'est pas déjà authentifié
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                // Charge les détails de l'utilisateur
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Vérifie la validité du token
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Crée le token d'authentification Spring Security
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // Ajoute les détails de la requête à l'authentification
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Définit l'utilisateur comme authentifié dans le contexte de sécurité
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Passe au filtre suivant
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            // Gère les exceptions via le resolver
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}