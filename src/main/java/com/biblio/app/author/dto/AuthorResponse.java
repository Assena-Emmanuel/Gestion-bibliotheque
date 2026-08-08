package com.biblio.app.author.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthorResponse {
    private UUID id;
    private String name;
    private String biography;
    private String nationality;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
