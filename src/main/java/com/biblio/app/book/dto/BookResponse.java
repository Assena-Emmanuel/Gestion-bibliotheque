package com.biblio.app.book.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.biblio.app.author.dto.AuthorResponse;
import com.biblio.app.categories.dto.CategorieResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Builder
@Getter
public class BookResponse {
    private UUID id;
    private String title;
    private String description;
    private String isbn;
    private Integer totalCopies;
    private Integer availableCopies;
    private String publisher; // Editeur
    private Integer publicationYear; // Année de publication
    private String language; // Langue
    private AuthorResponse author; // Auteur
    private CategorieResponse categoryId; // ID de la catégorie
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
