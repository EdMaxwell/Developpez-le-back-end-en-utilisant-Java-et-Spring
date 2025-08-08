package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.dtos.LoginUserDto;
import com.openclassrooms.chatop.dtos.RegisterUserDto;
import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void signupShouldSaveUserWithEncodedPassword() {
        RegisterUserDto input = new RegisterUserDto("john.doe@example.com", "password123", "John Doe");
        User savedUser = new User();
        savedUser.setName("John Doe");
        savedUser.setEmail("john.doe@example.com");
        savedUser.setPassword("encodedPassword");

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authenticationService.signup(input);

        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void authenticateShouldReturnUserWhenCredentialsAreValid() {
        LoginUserDto input = new LoginUserDto("john.doe@example.com", "password123");
        User user = new User();
        user.setEmail("john.doe@example.com");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        User result = authenticationService.authenticate(input);

        assertEquals("john.doe@example.com", result.getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticateShouldThrowExceptionWhenUserNotFound() {
        LoginUserDto input = new LoginUserDto("unknown@example.com", "password123");

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> authenticationService.authenticate(input));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}