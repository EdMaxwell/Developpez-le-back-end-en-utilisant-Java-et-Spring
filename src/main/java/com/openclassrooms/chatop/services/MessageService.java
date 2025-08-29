package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.dtos.CreateMessageDto;
import com.openclassrooms.chatop.entities.Message;
import com.openclassrooms.chatop.entities.Rental;
import com.openclassrooms.chatop.repositories.MessageRepository;
import com.openclassrooms.chatop.repositories.RentalRepository;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des messages entre utilisateurs.
 */
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;

    /**
     * Constructeur du service MessageService.
     *
     * @param messageRepository le repository des messages
     * @param rentalRepository le repository des locations
     */
    public MessageService(MessageRepository messageRepository, RentalRepository rentalRepository) {
        this.messageRepository = messageRepository;
        this.rentalRepository = rentalRepository;
    }

    /**
     * Envoie un message à l'utilisateur propriétaire d'une location.
     *
     * @param dto DTO contenant le contenu du message et l'identifiant de la location
     * @param senderId identifiant de l'utilisateur expéditeur (authentifié)
     * @return le message créé et sauvegardé, ou null si la location n'existe pas
     */
    public Message sendMessage(CreateMessageDto dto, Long senderId) {
        Rental rental = rentalRepository.findById(dto.getRental_id()).orElse(null);
        if (rental == null) return null;
        Message message = new Message();
        message.setContent(dto.getMessage());
        message.setSenderId(senderId);
        message.setRecipientId(rental.getOwnerId());
        message.setRentalId(rental.getId());
        return messageRepository.save(message);
    }
}