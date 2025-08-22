package com.openclassrooms.chatop.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * DTO pour la création d'une location.
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class CreateRentalDto {

    /** Nom de la location. */
    @NotBlank
    private String name;

    /** Surface en m². */
    @Positive
    private double surface;

    /** Prix de la location. */
    @Positive @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    /** Description de la location. */
    @NotBlank
    private String description;

    /** Fichier image associé à la location. */
    @NotNull
    private MultipartFile picture;
}