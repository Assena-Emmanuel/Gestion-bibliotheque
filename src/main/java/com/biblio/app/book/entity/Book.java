package com.biblio.app.book.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.biblio.app.author.entity.Author;
import com.biblio.app.categories.entity.Categorie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@AllArgsConstructor 
@NoArgsConstructor 
@Setter 
@Getter @Builder
public class Book {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 13)
    private String isbn;

    private String description;

    private String publisher; // Editeur

    private Integer publicationYear; // Année de publication

    private String language; // Langue

    @Column(nullable = false)
    private Integer availableCopies; // Nombre d'exemplaires disponibles

    @Column(nullable = false)
    private Integer totalCopies; // Nombre total d'exemplaires

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author; // Auteur

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categorie category; // Catégorie

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
