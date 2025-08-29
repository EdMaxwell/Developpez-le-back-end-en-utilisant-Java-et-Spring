package com.openclassrooms.chatop.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRentalDto {
    @NotBlank
    private String name;

    @Positive
    private double surface;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotBlank
    private String description;
}