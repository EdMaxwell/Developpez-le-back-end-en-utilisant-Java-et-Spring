package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.CreateRentalDto;
import com.openclassrooms.chatop.dtos.RentalListItemDto;
import com.openclassrooms.chatop.dtos.RentalListResponse;
import com.openclassrooms.chatop.dtos.RentalResponse;
import com.openclassrooms.chatop.entities.Rental;
import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.services.RentalService;
import com.openclassrooms.chatop.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des locations.
 */
@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;
    private final UserService userService;

    /**
     * Constructeur du contrôleur RentalController.
     * @param rentalService le service de gestion des locations
     */
    public RentalController(RentalService rentalService, UserService userService) {
        this.userService = userService;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        Rental saved = rentalService.createRental(dto, user.getId());
        return ResponseEntity
                .created(java.net.URI.create("/api/rentals/" + saved.getId()))
                .body(java.util.Map.of("message", "Rental created !", "id", saved.getId()));
    }

    /**
     * Endpoint GET pour récupérer une location par son ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RentalResponse> getRental(@PathVariable Long id) {
        Rental rental = rentalService.findById(id);
        if (rental == null) {
            return ResponseEntity.notFound().build();
        }
        String pictureUrl = "/api/rentals/" + rental.getId() + "/picture";
        RentalResponse response = new RentalResponse(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                new String[] { pictureUrl },
                rental.getDescription(),
                rental.getOwnerId(),
                formatDate(rental.getCreatedAt()),
                formatDate(rental.getUpdatedAt())
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint GET pour récupérer l'image d'une location.
     */
    @GetMapping("/{id}/picture")
    public ResponseEntity<byte[]> getRentalPicture(@PathVariable Long id) {
        Rental rental = rentalService.findById(id);
        if (rental == null) {
            return ResponseEntity.notFound().build();
        }
        if (rental.getPicture() == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok()
                .header("Content-Type", rental.getPictureContentType())
                .header("Content-Disposition", "inline; filename=\"" + rental.getPictureFilename() + "\"")
                .body(rental.getPicture());
    }

    /**
     * Endpoint GET pour récupérer la liste des rentals.
     */
    @GetMapping(produces = "application/json")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RentalListResponse> getAllRentals() {
        List<RentalListItemDto> rentals = rentalService.findAllRentals();
        return ResponseEntity.ok(new RentalListResponse(rentals));
    }

    /**
     * Formate la date au format yyyy/MM/dd.
     */
    private String formatDate(java.time.Instant instant) {
        return DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .withZone(ZoneId.systemDefault())
                .format(instant);
    }
}