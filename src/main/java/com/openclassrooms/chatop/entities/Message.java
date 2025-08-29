package com.openclassrooms.chatop.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * Entité représentant un message dans le système Chatop.
 * 
 * <p>Les messages permettent la communication entre les utilisateurs intéressés
 * par une location et les propriétaires de ces locations. Chaque message est
 * associé à une location spécifique et relie un expéditeur à un destinataire.</p>
 * 
 * <p>Structure d'un message :</p>
 * <ul>
 *   <li>Contenu textuel du message</li>
 *   <li>Identifiant de l'expéditeur</li>
 *   <li>Identifiant du destinataire (généralement le propriétaire)</li>
 *   <li>Identifiant de la location concernée</li>
 *   <li>Horodatage automatique de l'envoi</li>
 * </ul>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "messages")
public class Message {
    
    /**
     * Identifiant unique du message.
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 
     * Contenu du message. 
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /** 
     * Identifiant de l'utilisateur expéditeur du message.
     */
    @Column(nullable = false)
    private Long senderId;

    /** 
     * Identifiant de l'utilisateur destinataire (propriétaire du rental).
     */
    @Column(nullable = false)
    private Long recipientId;

    /** 
     * Identifiant de la location concernée par le message.
     */
    @Column(nullable = false)
    private Long rentalId;

    /** 
     * Date et heure d'envoi du message.
     * Définie automatiquement lors de la création.
     */
    @Column(nullable = false, updatable = false)
    private Instant sentAt;

    /**
     * Méthode appelée automatiquement avant la persistance de l'entité.
     * Initialise la date d'envoi du message.
     */
    @PrePersist
    void onSend() {
        this.sentAt = Instant.now();
    }
}