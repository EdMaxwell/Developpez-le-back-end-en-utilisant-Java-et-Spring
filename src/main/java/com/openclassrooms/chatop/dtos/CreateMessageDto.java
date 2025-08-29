package com.openclassrooms.chatop.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO pour la création d'un nouveau message.
 * 
 * <p>Ce DTO est utilisé pour transférer les informations nécessaires
 * lors de l'envoi d'un message concernant une location spécifique.</p>
 * 
 * <p>Le message relie un utilisateur expéditeur au propriétaire d'une location
 * via l'identifiant de la location.</p>
 * 
 * <p>Champs requis :</p>
 * <ul>
 *   <li>Message : contenu textuel du message</li>
 *   <li>User_id : identifiant du destinataire</li>
 *   <li>Rental_id : identifiant de la location concernée</li>
 * </ul>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateMessageDto {
    
    /**
     * Contenu textuel du message à envoyer.
     */
    @NotBlank
    private String message;

    /**
     * Identifiant de l'utilisateur destinataire du message.
     * Généralement le propriétaire de la location.
     */
    @NotNull
    private Long user_id;

    /**
     * Identifiant de la location concernée par le message.
     */
    @NotNull
    private Long rental_id;
}