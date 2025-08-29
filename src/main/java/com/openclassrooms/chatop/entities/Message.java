package com.openclassrooms.chatop.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "messages")
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Contenu du message. */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /** Utilisateur expéditeur. */
    @Column(nullable = false)
    private Long senderId;

    /** Utilisateur destinataire (propriétaire du rental). */
    @Column(nullable = false)
    private Long recipientId;

    /** Location concernée. */
    @Column(nullable = false)
    private Long rentalId;

    /** Date d’envoi. */
    @Column(nullable = false, updatable = false)
    private Instant sentAt;

    @PrePersist
    void onSend() {
        this.sentAt = Instant.now();
    }
}