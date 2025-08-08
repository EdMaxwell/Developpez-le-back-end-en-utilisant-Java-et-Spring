package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Service pour gérer les opérations liées aux utilisateurs.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    /**
     * Constructeur pour initialiser le service avec un UserRepository.
     *
     * @param userRepository Le repository utilisé pour accéder aux données des utilisateurs.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Recherche un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur à rechercher.
     * @return L'utilisateur correspondant à l'email fourni.
     * @throws NoSuchElementException Si aucun utilisateur n'est trouvé avec l'email donné.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé avec l'email : " + email));
    }

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id L'identifiant de l'utilisateur à rechercher. Ne peut pas être null.
     * @return L'utilisateur correspondant à l'identifiant fourni.
     * @throws IllegalArgumentException Si l'identifiant est null.
     * @throws NoSuchElementException Si aucun utilisateur n'est trouvé avec l'identifiant donné.
     */
    public User findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'id ne peut pas être null");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé avec l'id : " + id));
    }
}