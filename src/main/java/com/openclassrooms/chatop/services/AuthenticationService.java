package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.dtos.LoginUserDto;
import com.openclassrooms.chatop.dtos.RegisterUserDto;
import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion de l'authentification et de l'inscription des utilisateurs.
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructeur du service d'authentification.
     *
     * @param userRepository le repository des utilisateurs
     * @param authenticationManager le gestionnaire d'authentification Spring Security
     * @param passwordEncoder l'encodeur de mots de passe
     */
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Inscrit un nouvel utilisateur à partir des informations fournies.
     *
     * @param input les informations d'inscription de l'utilisateur
     * @return l'utilisateur créé et sauvegardé
     */
    public User signup(RegisterUserDto input) {
        User user = new User();
        user.setName(input.getName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Authentifie un utilisateur à partir des informations de connexion.
     *
     * @param input les informations de connexion de l'utilisateur
     * @return l'utilisateur authentifié
     * @throws org.springframework.security.core.AuthenticationException si l'authentification échoue
     * @throws java.util.NoSuchElementException si l'utilisateur n'est pas trouvé
     */
    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getLogin(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getLogin())
                .orElseThrow();
    }
}