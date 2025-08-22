package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.CreateRentalDto;
import com.openclassrooms.chatop.entities.Rental;
import com.openclassrooms.chatop.services.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des locations.
 */
@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;

    /**
     * Constructeur du contrôleur RentalController.
     * @param rentalService le service de gestion des locations
     */
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * Endpoint POST pour créer une nouvelle location.
     *
     * @param dto DTO contenant les informations et le fichier image
     * @return Réponse HTTP avec message de succès et l'ID créé
     * @throws Exception en cas d'erreur lors de la création
     */
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<?> create(@ModelAttribute @Valid CreateRentalDto dto) throws Exception {
        Rental saved = rentalService.createRental(dto);
        return ResponseEntity
                .created(java.net.URI.create("/api/rentals/" + saved.getId()))
                .body(java.util.Map.of("message", "Rental created !", "id", saved.getId()));
    }
}