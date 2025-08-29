package com.openclassrooms.chatop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entité représentant une location (rental).
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "rentals")
public class Rental {
    /** Identifiant unique de la location. */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom de la location. */
    private String name;

    /** Surface en m². */
    private double surface;

    /** Prix de la location. */
    private BigDecimal price;

    /** Description détaillée de la location. */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** Image de la location (stockée en BDD). */
    @Lob @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    @Column(columnDefinition = "LONGBLOB")
    private byte[] picture;

    /** Type MIME du fichier image. */
    private String pictureContentType;

    /** Nom du fichier image. */
    private String pictureFilename;

    /** Taille du fichier image en octets. */
    private long pictureSize;

    /** Date de création de la location. */
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    /** Initialise la date de création avant insertion. */
    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    /** Identifiant du propriétaire. */
    private Long ownerId;

    /** Date de dernière mise à jour de la location. */
    private Instant updatedAt;

    /** Met à jour la date de modification avant update. */
    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}