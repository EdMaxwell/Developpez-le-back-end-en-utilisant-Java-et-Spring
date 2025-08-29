package com.openclassrooms.chatop.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO pour les données de connexion d'un utilisateur.
 * 
 * <p>Ce DTO est utilisé pour transférer les informations d'authentification
 * nécessaires lors de la connexion d'un utilisateur existant.</p>
 * 
 * <p>Champs requis :</p>
 * <ul>
 *   <li>Email : identifiant unique de l'utilisateur</li>
 *   <li>Mot de passe : mot de passe de l'utilisateur</li>
 * </ul>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {
    
    /**
     * Adresse email de l'utilisateur.
     * Utilisée comme nom d'utilisateur pour l'authentification.
     */
    @NotBlank
    private String email;

    /**
     * Mot de passe de l'utilisateur.
     */
    @NotBlank
    private String password;
}