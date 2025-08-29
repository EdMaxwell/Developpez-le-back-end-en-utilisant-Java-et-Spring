package com.openclassrooms.chatop.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Entité représentant un utilisateur dans le système Chatop.
 * 
 * <p>Cette classe implémente UserDetails pour l'intégration avec Spring Security,
 * permettant l'authentification et l'autorisation des utilisateurs.</p>
 * 
 * <p>Chaque utilisateur possède :</p>
 * <ul>
 *   <li>Un identifiant unique</li>
 *   <li>Un nom d'affichage</li>
 *   <li>Une adresse email unique (utilisée comme nom d'utilisateur)</li>
 *   <li>Un mot de passe encodé</li>
 *   <li>Des dates de création et modification automatiques</li>
 * </ul>
 * 
 * @author OpenClassrooms
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    /**
     * Identifiant unique de l'utilisateur.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    /**
     * Nom d'affichage de l'utilisateur.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Adresse email de l'utilisateur, utilisée comme nom d'utilisateur.
     * Doit être unique dans le système.
     */
    @Column(unique = true, length = 100, nullable = false)
    private String email;

    /**
     * Mot de passe encodé de l'utilisateur.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Date de création du compte utilisateur.
     * Définie automatiquement lors de la première sauvegarde.
     */
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    /**
     * Date de dernière modification du compte utilisateur.
     * Mise à jour automatiquement à chaque sauvegarde.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * {@inheritDoc}
     * 
     * <p>Retourne une collection vide car aucun rôle spécifique n'est implémenté
     * pour le moment.</p>
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // pas de rôle pour le moment
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Retourne l'email de l'utilisateur qui sert de nom d'utilisateur.</p>
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * {@inheritDoc}
     * 
     * @return true (les comptes n'expirent pas)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @return true (les comptes ne sont pas verrouillés)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @return true (les identifiants n'expirent pas)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @return true (tous les utilisateurs sont activés)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
