package com.biblio.app.book.dto;

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
public class BookRequest {
    private String title;
    private String description;
    private String isbn;
    private Integer totalCopies;
    private Integer availableCopies;
    private String publisher; // Editeur
    private Integer publicationYear; // Année de publication
    private String language; // Langue
    private UUID authorId; // ID de l'auteur
    private UUID categoryId; // ID de la catégorie
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
