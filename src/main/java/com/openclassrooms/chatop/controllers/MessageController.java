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

/**
 * Contrôleur REST pour la gestion des messages.
 * 
 * <p>Ce contrôleur fournit les endpoints pour envoyer des messages concernant
 * des locations. Les messages permettent aux utilisateurs intéressés de contacter
 * les propriétaires des locations.</p>
 * 
 * <p>Endpoints disponibles :</p>
 * <ul>
 *   <li>POST /api/messages : Envoie un message au propriétaire d'une location</li>
 * </ul>
 * 
 * <p>Sécurité : Tous les endpoints requièrent une authentification.</p>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    
    private final MessageService messageService;
    private final UserService userService;

    /**
     * Constructeur du contrôleur des messages.
     * 
     * @param messageService le service de gestion des messages
     * @param userService le service de gestion des utilisateurs
     */
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    /**
     * Endpoint POST pour envoyer un message au propriétaire d'une location.
     * 
     * <p>Permet à un utilisateur authentifié d'envoyer un message concernant
     * une location spécifique. Le message sera automatiquement associé à
     * l'expéditeur (utilisateur courant) et au destinataire spécifié.</p>
     * 
     * <p>Processus :</p>
     * <ol>
     *   <li>Vérification de l'authentification de l'utilisateur</li>
     *   <li>Récupération des informations de l'utilisateur authentifié</li>
     *   <li>Envoi du message via le service</li>
     *   <li>Retour d'une confirmation</li>
     * </ol>
     * 
     * @param dto les données du message à envoyer, incluant le contenu,
     *            l'ID du destinataire et l'ID de la location
     * @return ResponseEntity avec un message de confirmation si succès,
     *         401 si l'utilisateur n'est pas authentifié,
     *         400 si la location n'existe pas
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> sendMessage(@RequestBody @Valid CreateMessageDto dto) {
        // Récupération de l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        // Envoi du message
        Message message = messageService.sendMessage(dto, user.getId());
        if (message == null) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Rental not found"));
        }
        
        return ResponseEntity.ok(java.util.Map.of("message", "Message sent !"));
    }
}