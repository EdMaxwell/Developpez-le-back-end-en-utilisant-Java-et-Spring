package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.dtos.CreateRentalDto;
import com.openclassrooms.chatop.entities.Rental;
import com.openclassrooms.chatop.repositories.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service pour la gestion des locations.
 */
@Service
public class RentalService {
    /** Taille maximale autorisée pour l'image (5 Mo). */
    private static final long MAX_BYTES = 5L * 1024 * 1024;

    private final RentalRepository repo;

    /**
     * Constructeur du service RentalService.
     * @param repo le repository JPA pour Rental
     */
    public RentalService(RentalRepository repo) { this.repo = repo; }

    /**
     * Crée une nouvelle location à partir du DTO.
     *
     * @param dto le DTO de création
     * @return la location créée
     * @throws IOException si la lecture du fichier échoue
     * @throws IllegalArgumentException si le fichier est trop volumineux ou n'est pas une image
     */
    @org.springframework.transaction.annotation.Transactional
    public Rental createRental(@jakarta.validation.Valid CreateRentalDto dto, Long ownerId) throws IOException {
        Rental r = new Rental();
        r.setOwnerId(ownerId);
        r.setName(dto.getName());
        r.setSurface(dto.getSurface());
        r.setPrice(dto.getPrice());
        r.setDescription(dto.getDescription());

        MultipartFile pic = dto.getPicture();
        if (pic != null && !pic.isEmpty()) {
            if (pic.getSize() > MAX_BYTES) throw new IllegalArgumentException("File too large (max 5MB)");
            String ct = String.valueOf(pic.getContentType());
            if (!ct.startsWith("image/")) throw new IllegalArgumentException("File must be an image");
            r.setPicture(pic.getBytes());
            r.setPictureContentType(ct);
            r.setPictureFilename(java.util.Optional.ofNullable(pic.getOriginalFilename()).orElse("image"));
            r.setPictureSize(pic.getSize());
        }
        return repo.save(r);
    }

    /**
     * Récupère une location par son identifiant.
     * @param id identifiant de la location
     * @return la location ou null si non trouvée
     */
    public Rental findById(Long id) {
        return repo.findById(id).orElse(null);
    }
}