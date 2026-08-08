package com.biblio.app.categories.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.biblio.app.categories.dto.CategorieRequest;
import com.biblio.app.categories.dto.CategorieResponse;
import com.biblio.app.categories.entity.Categorie;
import com.biblio.app.categories.repository.CategorieRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategorieService {
    private final CategorieRepository categorieRepository;

    public CategorieResponse createCategorie(CategorieRequest categorieRequest) {

        Categorie categorie = new Categorie();
        categorie.setName(categorieRequest.getName());
        categorie.setDescription(categorieRequest.getDescription());    

        Categorie savedCategorie = categorieRepository.save(categorie);

        return toResponse(savedCategorie);
    }

    public List<CategorieResponse> createCategories(List<CategorieRequest> categorieRequests) {
        return categorieRequests.stream()
                .map(this::createCategorie)
                .toList();
    }

    public Optional<CategorieResponse> getCategorieById(UUID id) {
        if (!categorieRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        return categorieRepository.findById(id).map(this::toResponse);
    }

    public List<CategorieResponse> getAllCategories() {
        return categorieRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public void deleteCategorieById(UUID id) {
        if (!categorieRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categorieRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return categorieRepository.existsById(id);
    }

    public boolean existsByName(String name) {
        return categorieRepository.existsByName(name);
    }

    public CategorieResponse updateCategorie(UUID id, CategorieRequest categorieRequest) {
        Categorie existingCategorie = categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if(categorieRequest.getName() != null && !categorieRequest.getName().isEmpty()) {
            existingCategorie.setName(categorieRequest.getName());
        }

        if(categorieRequest.getDescription() != null && !categorieRequest.getDescription().isEmpty()) {
            existingCategorie.setDescription(categorieRequest.getDescription());
        }

        Categorie updatedCategorie = categorieRepository.save(existingCategorie);

        return toResponse(updatedCategorie);
    }


    private CategorieResponse toResponse(Categorie categorie) {
        return new CategorieResponse(
            categorie.getId(),
            categorie.getName(),
            categorie.getDescription(),
            categorie.getCreatedAt(),
            categorie.getUpdatedAt()
        );
    }

    public Categorie getCategorieEntityById(UUID categoryId) {
        return categorieRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
    }
}

