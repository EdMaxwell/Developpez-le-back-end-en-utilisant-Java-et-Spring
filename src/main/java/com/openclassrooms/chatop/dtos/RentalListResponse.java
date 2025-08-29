package com.openclassrooms.chatop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour la réponse de la liste des rentals.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalListResponse {
    private List<RentalListItemDto> rentals;
}