package com.openclassrooms.chatop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO pour la réponse d'une location.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponse {
    private Long id;
    private String name;
    private double surface;
    private BigDecimal price;
    private String[] picture;
    private String description;
    private Long owner_id;
    private String created_at;
    private String updated_at;
}