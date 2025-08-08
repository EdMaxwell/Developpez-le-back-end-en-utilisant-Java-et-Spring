package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.LoginUserDto;
import com.openclassrooms.chatop.dtos.LoginResponse;
import com.openclassrooms.chatop.dtos.MeResponse;
import com.openclassrooms.chatop.dtos.RegisterUserDto;
import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.services.AuthenticationService;
import com.openclassrooms.chatop.services.JwtService;
import com.openclassrooms.chatop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Contrôleur pour la gestion de l'authentification des utilisateurs.
 */
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    /**
     * Constructeur du contrôleur d'authentification.
     *
     * @param jwtService le service de gestion des JWT
     * @param authenticationService le service d'authentification
     */
    public AuthenticationController(
            JwtService jwtService,
            AuthenticationService authenticationService,
            UserService userService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     * Inscrit un nouvel utilisateur.
     *
     * @param registerUserDto les informations d'inscription de l'utilisateur
     * @return l'utilisateur créé
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Authentifie un utilisateur et retourne un token JWT.
     *
     * @param loginUserDto les informations de connexion de l'utilisateur
     * @return la réponse contenant le token JWT et la durée d'expiration
     */
    @PostMapping("/email")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Récupère les informations de l'utilisateur actuellement authentifié.
     *
     * @return une réponse contenant les informations de l'utilisateur sous forme de `MeResponse`
     *         ou un code d'erreur HTTP (401 si non authentifié, 404 si l'utilisateur n'est pas trouvé).
     */
    @GetMapping("/me")
    public ResponseEntity<MeResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).build(); // Retourne 401 Unauthorized si non authentifié
        }

        String email = authentication.getName();

        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(404).build(); // Retourne 404 Not Found si l'utilisateur n'est pas trouvé
        }

        MeResponse response = new MeResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        return ResponseEntity.ok(response);
    }
}