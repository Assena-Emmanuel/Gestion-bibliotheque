package com.biblio.app.author.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRequest {
    private String name;
    private String biography;
    private String nationality;
    private LocalDate birthDate;
    private LocalDate deathDate;
}
