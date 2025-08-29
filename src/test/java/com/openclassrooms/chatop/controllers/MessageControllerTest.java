package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.CreateMessageDto;
import com.openclassrooms.chatop.entities.Message;
import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.services.MessageService;
import com.openclassrooms.chatop.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MessageController messageController;

    private CreateMessageDto messageDto;
    private User user;
    private Message message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        messageDto = new CreateMessageDto();
        messageDto.setMessage("Hello, I'm interested in your rental!");
        messageDto.setUser_id(1L);
        messageDto.setRental_id(1L);

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        user.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        message = new Message();
        message.setId(1L);
        message.setContent("Hello, I'm interested in your rental!");
        message.setSenderId(1L);
        message.setRecipientId(2L);
        message.setRentalId(1L);
        message.setSentAt(Instant.now());
    }

    @Test
    void sendMessage_ShouldReturnSuccess_WhenValidMessage() {
        // Given
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(messageService.sendMessage(any(CreateMessageDto.class), eq(1L))).thenReturn(message);

        // When
        ResponseEntity<?> response = messageController.sendMessage(messageDto);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Message sent !", responseBody.get("message"));

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    @Test
    void sendMessage_ShouldReturnBadRequest_WhenRentalNotFound() {
        // Given
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(messageService.sendMessage(any(CreateMessageDto.class), eq(1L))).thenReturn(null);

        // When
        ResponseEntity<?> response = messageController.sendMessage(messageDto);

        // Then
        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Rental not found", responseBody.get("message"));

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    @Test
    void sendMessage_ShouldReturnUnauthorized_WhenUserNotFound() {
        // Given
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("unknown@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail("unknown@example.com")).thenReturn(null);

        // When
        ResponseEntity<?> response = messageController.sendMessage(messageDto);

        // Then
        assertEquals(401, response.getStatusCodeValue());
        assertNull(response.getBody());

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    @Test
    void sendMessage_ShouldUseAuthenticatedUserEmail() {
        // Given
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("different@example.com");
        SecurityContextHolder.setContext(securityContext);

        User differentUser = new User();
        differentUser.setId(99L);
        differentUser.setEmail("different@example.com");
        
        when(userService.findByEmail("different@example.com")).thenReturn(differentUser);
        when(messageService.sendMessage(any(CreateMessageDto.class), eq(99L))).thenReturn(message);

        // When
        ResponseEntity<?> response = messageController.sendMessage(messageDto);

        // Then
        assertEquals(200, response.getStatusCodeValue());

        // Cleanup
        SecurityContextHolder.clearContext();
    }
}