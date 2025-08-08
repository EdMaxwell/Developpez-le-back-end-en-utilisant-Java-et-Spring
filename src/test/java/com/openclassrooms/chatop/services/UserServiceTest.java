package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void findByEmail_ReturnsUser_WhenEmailExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User result = userService.findByEmail(user.getEmail());

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getName(), result.getName());
    }

    @Test
    void findByEmail_ThrowsException_WhenEmailDoesNotExist() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findByEmail("nonexistent@example.com"));
    }

    @Test
    void findById_ReturnsUser_WhenIdExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = userService.findById(user.getId());

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
    }

    @Test
    void findById_ThrowsException_WhenIdDoesNotExist() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findById(2L));
    }

    @Test
    void findById_ThrowsException_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.findById(null));
    }
}