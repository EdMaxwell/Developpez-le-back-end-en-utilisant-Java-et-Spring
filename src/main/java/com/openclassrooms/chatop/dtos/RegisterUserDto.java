package com.openclassrooms.chatop.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO pour les données d'inscription d'un nouvel utilisateur.
 * 
 * <p>Ce DTO est utilisé pour transférer les informations nécessaires
 * lors de la création d'un nouveau compte utilisateur.</p>
 * 
 * <p>Champs requis :</p>
 * <ul>
 *   <li>Email : doit être une adresse email valide et unique</li>
 *   <li>Mot de passe : minimum 6 caractères</li>
 *   <li>Nom : nom d'affichage de l'utilisateur</li>
 * </ul>
 * 
 * <p>Validation appliquée :</p>
 * <ul>
 *   <li>Format email valide</li>
 *   <li>Tous les champs obligatoires</li>
 *   <li>Longueur minimale du mot de passe</li>
 * </ul>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
    
    /**
     * Adresse email de l'utilisateur.
     * Doit être unique et respecter le format email valide.
     */
    @NotBlank
    @Email
    private String email;

    /**
     * Mot de passe de l'utilisateur.
     * Doit contenir au minimum 6 caractères.
     */
    @NotBlank
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    /**
     * Nom d'affichage de l'utilisateur.
     */
    @NotBlank
    private String name;
}