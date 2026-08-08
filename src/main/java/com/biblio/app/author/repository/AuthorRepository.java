package com.biblio.app.author.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblio.app.author.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    boolean existsByName(String name);
    Optional<Author> findByName(String name);
}
