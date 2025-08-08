package com.openclassrooms.chatop.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeResponse {
    private Long id;
    private String name;
    private String email;
    private Date created_at;
    private Date updated_at;
}
