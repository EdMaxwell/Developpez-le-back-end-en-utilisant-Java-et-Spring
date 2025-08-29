package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.dtos.CreateRentalDto;
import com.openclassrooms.chatop.dtos.RentalListItemDto;
import com.openclassrooms.chatop.dtos.UpdateRentalDto;
import com.openclassrooms.chatop.entities.Rental;
import com.openclassrooms.chatop.repositories.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
            r.setPictureFilename(java.util.UUID.randomUUID() + getExtension(pic.getOriginalFilename()));
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

    /**
     * Récupère tous les rentals et les mappe en DTO.
     * @return liste des rentals au format DTO
     */
    public List<RentalListItemDto> findAllRentals() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(ZoneId.systemDefault());
        return repo.findAll().stream()
                .map(rental -> new RentalListItemDto(
                        rental.getId(),
                        rental.getName(),
                        rental.getSurface(),
                        rental.getPrice(),
                        "/api/rentals/" + rental.getId() + "/picture", // URL de l'image
                        rental.getDescription(),
                        rental.getOwnerId(),
                        formatter.format(rental.getCreatedAt()),
                        formatter.format(rental.getUpdatedAt())
                ))
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une location existante.
     * @param id identifiant de la location à mettre à jour
     * @param dto données de mise à jour
     * @param userId identifiant de l'utilisateur effectuant la mise à jour
     * @return la location mise à jour ou null si non trouvée ou pas le propriétaire
     */
    public Rental updateRental(Long id, UpdateRentalDto dto, Long userId) {
        Rental rental = findById(id);
        if (rental == null || !rental.getOwnerId().equals(userId)) {
            return null;
        }
        rental.setName(dto.getName());
        rental.setSurface(dto.getSurface());
        rental.setPrice(dto.getPrice());
        rental.setDescription(dto.getDescription());
        return repo.save(rental);
    }

    private String getExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        return (idx > 0) ? filename.substring(idx) : "";
    }


}