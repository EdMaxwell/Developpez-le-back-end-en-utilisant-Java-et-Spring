package com.openclassrooms.chatop.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;
    private String validToken;

    @BeforeEach
    void setUp() {
        // Set up properties for JwtService
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L); // 24 hours

        // Create test user details
        userDetails = new User("test@example.com", "password", new ArrayList<>());

        // Generate a valid token for testing
        validToken = jwtService.generateToken(userDetails);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts separated by dots
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        String username = jwtService.extractUsername(validToken);

        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    void isTokenValid_ShouldReturnTrue_WhenTokenIsValidForUser() {
        boolean isValid = jwtService.isTokenValid(validToken, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_ShouldReturnFalse_WhenTokenIsForDifferentUser() {
        UserDetails differentUser = new User("different@example.com", "password", new ArrayList<>());

        boolean isValid = jwtService.isTokenValid(validToken, differentUser);

        assertFalse(isValid);
    }

    @Test
    void getExpirationTime_ShouldReturnConfiguredExpirationTime() {
        long expirationTime = jwtService.getExpirationTime();

        assertEquals(86400000L, expirationTime);
    }

    @Test
    void generateToken_WithExtraClaims_ShouldCreateValidToken() {
        java.util.Map<String, Object> extraClaims = new java.util.HashMap<>();
        extraClaims.put("role", "USER");

        String token = jwtService.generateToken(extraClaims, userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Verify username can still be extracted
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), extractedUsername);
    }

    @Test
    void extractClaim_ShouldExtractSubjectCorrectly() {
        String subject = jwtService.extractClaim(validToken, claims -> claims.getSubject());

        assertEquals(userDetails.getUsername(), subject);
    }

    @Test
    void isTokenValid_ShouldReturnFalse_WhenTokenIsExpired() {
        // Create a token with very short expiration time
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1L); // 1 millisecond

        String expiredToken = jwtService.generateToken(userDetails);

        // Wait a moment for token to expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // The JWT library throws ExpiredJwtException when parsing expired tokens
        // This means the token validation will return false for expired tokens
        boolean isValid;
        try {
            isValid = jwtService.isTokenValid(expiredToken, userDetails);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // This is expected behavior - expired tokens should cause an exception
            isValid = false;
        }

        assertFalse(isValid);
    }
}