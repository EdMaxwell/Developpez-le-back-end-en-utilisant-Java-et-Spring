package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.MeResponse;
import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Récupère les informations d'un utilisateur par son id.
     *
     * @param id l'identifiant de l'utilisateur
     * @return les informations de l'utilisateur ou 404 si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<MeResponse> getUserById(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            MeResponse response = new MeResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}