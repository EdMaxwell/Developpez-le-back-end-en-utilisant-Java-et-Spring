package com.openclassrooms.chatop.repositories;

import com.openclassrooms.chatop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository JPA pour l'entité User.
 * 
 * <p>Cette interface étend JpaRepository et fournit les opérations CRUD de base
 * pour l'entité User, ainsi que des méthodes de recherche personnalisées.</p>
 * 
 * <p>Méthodes disponibles :</p>
 * <ul>
 *   <li>Toutes les méthodes héritées de JpaRepository (save, findById, findAll, delete, etc.)</li>
 *   <li>findByEmail : recherche d'un utilisateur par son adresse email</li>
 * </ul>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Recherche un utilisateur par son adresse email.
     * 
     * <p>Cette méthode est utilisée principalement pour l'authentification
     * car l'email sert d'identifiant unique dans le système.</p>
     * 
     * @param email l'adresse email de l'utilisateur à rechercher
     * @return Optional contenant l'utilisateur si trouvé, Optional vide sinon
     */
    Optional<User> findByEmail(String email);
}
