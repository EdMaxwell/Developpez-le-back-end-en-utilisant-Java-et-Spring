package com.openclassrooms.chatop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO représentant un élément de location dans une liste.
 * 
 * <p>Ce DTO est utilisé pour afficher les informations condensées d'une location
 * dans la liste des locations disponibles. Il contient toutes les informations
 * essentielles sans les détails complets.</p>
 * 
 * <p>Informations incluses :</p>
 * <ul>
 *   <li>Identifiant unique de la location</li>
 *   <li>Nom/titre de la location</li>
 *   <li>Surface en mètres carrés</li>
 *   <li>Prix de la location</li>
 *   <li>URL de l'image de prévisualisation</li>
 *   <li>Description</li>
 *   <li>Identifiant du propriétaire</li>
 *   <li>Dates de création et modification (format string)</li>
 * </ul>
 * 
 * <p>L'image est fournie sous forme d'URL pointant vers l'endpoint
 * de récupération d'image de la location.</p>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalListItemDto {
    
    /**
     * Identifiant unique de la location.
     */
    private Long id;
    
    /**
     * Nom/titre de la location.
     */
    private String name;
    
    /**
     * Surface de la location en mètres carrés.
     */
    private double surface;
    
    /**
     * Prix de la location.
     */
    private BigDecimal price;
    
    /**
     * URL de l'image de prévisualisation de la location.
     * Pointe généralement vers l'endpoint /api/rentals/{id}/picture.
     */
    private String picture;
    
    /**
     * Description de la location.
     */
    private String description;
    
    /**
     * Identifiant du propriétaire de la location.
     */
    private Long owner_id;
    
    /**
     * Date de création de la location au format string.
     */
    private String created_at;
    
    /**
     * Date de dernière modification de la location au format string.
     */
    private String updated_at;
}