package com.openclassrooms.chatop.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO pour la mise à jour d'une location existante.
 * 
 * <p>Ce DTO est utilisé pour transférer les informations modifiées
 * d'une location existante. Contrairement à la création, aucune image
 * n'est incluse dans cette opération.</p>
 * 
 * <p>Champs modifiables :</p>
 * <ul>
 *   <li>Nom : nom/titre de la location</li>
 *   <li>Surface : surface en m² (doit être positive)</li>
 *   <li>Prix : prix de la location (doit être positif)</li>
 *   <li>Description : description détaillée</li>
 * </ul>
 * 
 * <p>Contraintes de validation :</p>
 * <ul>
 *   <li>Tous les champs sont obligatoires</li>
 *   <li>Surface et prix doivent être positifs</li>
 *   <li>Prix doit respecter le format décimal (10 chiffres entiers, 2 décimales max)</li>
 * </ul>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRentalDto {
    
    /**
     * Nom de la location.
     */
    @NotBlank
    private String name;

    /**
     * Surface de la location en mètres carrés.
     * Doit être une valeur positive.
     */
    @Positive
    private double surface;

    /**
     * Prix de la location.
     * Doit être une valeur positive avec au maximum 10 chiffres entiers
     * et 2 chiffres après la virgule.
     */
    @NotNull
    @Positive
    private BigDecimal price;

    /**
     * Description détaillée de la location.
     */
    @NotBlank
    private String description;
}