package com.openclassrooms.chatop.repositories;

import com.openclassrooms.chatop.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA pour l'entité Message.
 * 
 * <p>Cette interface étend JpaRepository et fournit les opérations CRUD de base
 * pour l'entité Message. Elle permet de gérer la persistance des messages
 * échangés entre les utilisateurs concernant les locations.</p>
 * 
 * <p>Fonctionnalités disponibles :</p>
 * <ul>
 *   <li>Sauvegarde des messages</li>
 *   <li>Récupération des messages par ID</li>
 *   <li>Suppression des messages</li>
 *   <li>Listing de tous les messages</li>
 * </ul>
 * 
 * <p>Note : Des méthodes de recherche personnalisées peuvent être ajoutées
 * au besoin (par exemple, findBySenderId, findByRentalId, etc.).</p>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}