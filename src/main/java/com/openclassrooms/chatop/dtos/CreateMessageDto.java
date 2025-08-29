package com.openclassrooms.chatop.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateMessageDto {
    @NotBlank
    private String message;

    @NotNull
    private Long user_id;

    @NotNull
    private Long rental_id;
}