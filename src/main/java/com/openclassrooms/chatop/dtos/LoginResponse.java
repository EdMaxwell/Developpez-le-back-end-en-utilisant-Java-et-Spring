package com.openclassrooms.chatop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de réponse pour l'authentification d'un utilisateur.
 * 
 * <p>Ce DTO est retourné lors d'une connexion ou inscription réussie
 * et contient les informations nécessaires pour authentifier les
 * requêtes suivantes.</p>
 * 
 * <p>Contenu de la réponse :</p>
 * <ul>
 *   <li>Token JWT : utilisé dans l'header Authorization des requêtes suivantes</li>
 *   <li>Durée d'expiration : temps en millisecondes avant expiration du token</li>
 * </ul>
 * 
 * <p>Usage : le client doit inclure le token dans l'header Authorization
 * sous la forme "Bearer {token}" pour les endpoints protégés.</p>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    /**
     * Token JWT à utiliser pour l'authentification des requêtes suivantes.
     * Doit être inclus dans l'header Authorization sous la forme "Bearer {token}".
     */
    private String token;
    
    /**
     * Durée de validité du token en millisecondes.
     * Après cette durée, le token expire et l'utilisateur doit se reconnecter.
     */
    private long expiresIn;
}
