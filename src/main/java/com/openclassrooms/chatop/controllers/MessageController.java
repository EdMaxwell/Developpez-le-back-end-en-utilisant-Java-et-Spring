package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.CreateMessageDto;
import com.openclassrooms.chatop.entities.Message;
import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.services.MessageService;
import com.openclassrooms.chatop.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    /**
     * Endpoint POST pour envoyer un message au propriétaire d’une location.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> sendMessage(@RequestBody @Valid CreateMessageDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        Message message = messageService.sendMessage(dto, user.getId());
        if (message == null) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Rental not found"));
        }
        return ResponseEntity.ok(java.util.Map.of("message", "Message sent !"));
    }
}