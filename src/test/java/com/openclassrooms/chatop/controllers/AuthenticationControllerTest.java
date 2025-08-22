package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.LoginResponse;
import com.openclassrooms.chatop.dtos.LoginUserDto;
import com.openclassrooms.chatop.dtos.RegisterUserDto;
import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.services.AuthenticationService;
import com.openclassrooms.chatop.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private RegisterUserDto validRegisterUserDto;
    private RegisterUserDto invalidRegisterUserDto;
    private LoginUserDto validLoginUserDto;
    private LoginUserDto invalidLoginUserDto;
    private User mockUser;

    // Initialisation des objets communs avant chaque test
    @BeforeEach
    void setUp() {
        validRegisterUserDto = new RegisterUserDto("john.doe@example.com", "password123", "John Doe");
        invalidRegisterUserDto = new RegisterUserDto("", "", "");
        validLoginUserDto = new LoginUserDto("john.doe@example.com", "password123");
        invalidLoginUserDto = new LoginUserDto("invalid@example.com", "wrongPassword");

        mockUser = new User();
        mockUser.setName("John Doe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("encodedPassword");
    }

    // Vérifie que l'inscription retourne l'utilisateur créé avec des données valides
    @Test
    void registerShouldReturnJwtTokenWhenValidInput() {
        String mockToken = "mockJwtToken";
        when(authenticationService.signup(validRegisterUserDto)).thenReturn(mockUser);
        when(jwtService.generateToken(mockUser)).thenReturn(mockToken);
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        ResponseEntity<LoginResponse> response = authenticationController.register(validRegisterUserDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockToken, response.getBody().getToken());
        assertEquals(3600L, response.getBody().getExpiresIn());
    }

    // Vérifie que l'inscription échoue avec des données invalides
    @Test
    void registerShouldThrowValidationExceptionWhenInvalidInput() {
        // Ici, la validation @Valid n'est pas appliquée car on appelle la méthode directement
        // Il faudrait simuler le comportement ou adapter le test selon la logique métier
        when(authenticationService.signup(invalidRegisterUserDto)).thenThrow(new IllegalArgumentException("Invalid input"));

        assertThrows(IllegalArgumentException.class, () -> {
            authenticationController.register(invalidRegisterUserDto);
        });
    }

    // Vérifie que l'authentification retourne un token JWT avec des identifiants valides
    @Test
    void authenticateShouldReturnJwtTokenWhenValidCredentials() {
        String mockToken = "mockJwtToken";

        when(authenticationService.authenticate(validLoginUserDto)).thenReturn(mockUser);
        when(jwtService.generateToken(mockUser)).thenReturn(mockToken);
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        ResponseEntity<LoginResponse> response = authenticationController.authenticate(validLoginUserDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockToken, response.getBody().getToken());
        assertEquals(3600L, response.getBody().getExpiresIn());
    }

    // Vérifie que l'authentification échoue avec des identifiants invalides
    @Test
    void authenticateShouldThrowExceptionWhenInvalidCredentials() {
        when(authenticationService.authenticate(invalidLoginUserDto)).thenThrow(new RuntimeException("Invalid credentials"));

        assertThrows(RuntimeException.class, () -> {
            authenticationController.authenticate(invalidLoginUserDto);
        });
    }
}