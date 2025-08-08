package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.LoginUserDto;
import com.openclassrooms.chatop.dtos.LoginResponse;
import com.openclassrooms.chatop.dtos.RegisterUserDto;
import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.services.AuthenticationService;
import com.openclassrooms.chatop.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * Contrôleur pour la gestion de l'authentification des utilisateurs.
 */
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    /**
     * Constructeur du contrôleur d'authentification.
     *
     * @param jwtService le service de gestion des JWT
     * @param authenticationService le service d'authentification
     */
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
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
}