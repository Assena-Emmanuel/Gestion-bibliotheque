package com.biblio.app.categories.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblio.app.categories.entity.Categorie;

public interface CategorieRepository extends JpaRepository<Categorie, UUID> {
    boolean existsByName(String name);
    Optional<Categorie> findByName(String name);

}
