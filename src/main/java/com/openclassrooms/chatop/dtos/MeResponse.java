package com.openclassrooms.chatop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO de réponse pour les informations d'un utilisateur.
 * 
 * <p>Ce DTO est utilisé pour retourner les informations publiques d'un utilisateur,
 * que ce soit pour l'endpoint "/me" (utilisateur courant) ou pour récupérer
 * les informations d'un utilisateur spécifique.</p>
 * 
 * <p>Informations incluses :</p>
 * <ul>
 *   <li>Identifiant unique</li>
 *   <li>Nom d'affichage</li>
 *   <li>Adresse email</li>
 *   <li>Date de création du compte</li>
 *   <li>Date de dernière modification</li>
 * </ul>
 * 
 * <p>Note : Le mot de passe n'est jamais inclus dans ce DTO pour des raisons de sécurité.</p>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeResponse {
    
    /**
     * Identifiant unique de l'utilisateur.
     */
    private Long id;
    
    /**
     * Nom d'affichage de l'utilisateur.
     */
    private String name;
    
    /**
     * Adresse email de l'utilisateur.
     */
    private String email;
    
    /**
     * Date de création du compte utilisateur.
     */
    private Date created_at;
    
    /**
     * Date de dernière modification du compte.
     */
    private Date updated_at;
}
