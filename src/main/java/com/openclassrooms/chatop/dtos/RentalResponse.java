package com.openclassrooms.chatop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de réponse pour les détails complets d'une location.
 * 
 * <p>Ce DTO est utilisé pour retourner toutes les informations détaillées
 * d'une location spécifique, incluant les métadonnées et l'image.</p>
 * 
 * <p>Différences avec RentalListItemDto :</p>
 * <ul>
 *   <li>Contient un tableau d'URLs d'images (extensible pour plusieurs images)</li>
 *   <li>Utilisé pour la vue détail plutôt que la liste</li>
 *   <li>Peut inclure des informations supplémentaires si nécessaire</li>
 * </ul>
 * 
 * <p>Structure de la réponse :</p>
 * <pre>
 * {
 *   "id": 1,
 *   "name": "Villa by the sea",
 *   "surface": 120.5,
 *   "price": 2500.00,
 *   "picture": ["/api/rentals/1/picture"],
 *   "description": "Beautiful villa with sea view...",
 *   "owner_id": 15,
 *   "created_at": "2023/12/01",
 *   "updated_at": "2023/12/01"
 * }
 * </pre>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 * @see RentalListItemDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponse {
    
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
     * Tableau des URLs des images de la location.
     * 
     * <p>Actuellement contient une seule image mais la structure
     * permet l'extension pour plusieurs images à l'avenir.</p>
     */
    private String[] picture;
    
    /**
     * Description détaillée de la location.
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