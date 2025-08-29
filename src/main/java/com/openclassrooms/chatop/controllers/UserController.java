package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.MeResponse;
import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 * 
 * <p>Ce contrôleur fournit les endpoints pour récupérer les informations
 * des utilisateurs par leur identifiant.</p>
 * 
 * <p>Endpoints disponibles :</p>
 * <ul>
 *   <li>GET /api/user/{id} : Récupère les informations d'un utilisateur par son ID</li>
 * </ul>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /**
     * Constructeur du contrôleur des utilisateurs.
     * 
     * @param userService le service de gestion des utilisateurs
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Récupère les informations d'un utilisateur par son identifiant.
     *
     * <p>Retourne les informations publiques d'un utilisateur spécifique.
     * Les informations sensibles comme le mot de passe ne sont pas incluses.</p>
     *
     * @param id l'identifiant de l'utilisateur à récupérer
     * @return ResponseEntity contenant les informations de l'utilisateur ou 404 si non trouvé
     * @throws IllegalArgumentException si l'id est null
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