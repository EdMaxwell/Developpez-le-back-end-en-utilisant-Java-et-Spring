package com.openclassrooms.chatop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de réponse pour la liste des locations.
 * 
 * <p>Ce DTO encapsule la liste des locations retournée par l'endpoint
 * GET /api/rentals. Il permet de structurer la réponse de manière cohérente
 * avec la structure attendue par l'interface client.</p>
 * 
 * <p>Structure de la réponse :</p>
 * <pre>
 * {
 *   "rentals": [
 *     {
 *       "id": 1,
 *       "name": "Villa by the sea",
 *       "surface": 120.5,
 *       "price": 2500.00,
 *       "picture": "/api/rentals/1/picture",
 *       "description": "Beautiful villa...",
 *       "owner_id": 15,
 *       "created_at": "2023/12/01",
 *       "updated_at": "2023/12/01"
 *     }
 *   ]
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
public class RentalListResponse {
    
    /**
     * Liste des locations disponibles.
     * 
     * <p>Chaque élément de la liste est un RentalListItemDto contenant
     * les informations essentielles d'une location pour l'affichage
     * dans une liste ou grille.</p>
     */
    private List<RentalListItemDto> rentals;
}