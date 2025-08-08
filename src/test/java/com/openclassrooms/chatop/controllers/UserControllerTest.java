package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.MeResponse;
import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        user.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
    }

    @Test
    void getUserById_ReturnsUser_WhenUserExists() {
        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<MeResponse> response = userController.getUserById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(user.getId(), response.getBody().getId());
        assertEquals(user.getName(), response.getBody().getName());
        assertEquals(user.getEmail(), response.getBody().getEmail());
    }

    @Test
    void getUserById_ReturnsNotFound_WhenUserDoesNotExist() {
        when(userService.findById(2L)).thenThrow(new NoSuchElementException());

        ResponseEntity<MeResponse> response = userController.getUserById(2L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}