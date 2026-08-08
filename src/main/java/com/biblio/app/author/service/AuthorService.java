package com.biblio.app.author.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.biblio.app.author.dto.AuthorRequest;
import com.biblio.app.author.dto.AuthorResponse;
import com.biblio.app.author.entity.Author;
import com.biblio.app.author.repository.AuthorRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public void deleteAuthorById(UUID id) {
        authorRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return authorRepository.existsById(id);
    }

    public AuthorResponse createAuthor(AuthorRequest request) {

        if (authorRepository.existsByName(request.getName())) {
            throw new RuntimeException(
                "Author already exists with name: " + request.getName()
            );
        }

        Author author = new Author();

        author.setName(request.getName());
        author.setBiography(request.getBiography());
        author.setNationality(request.getNationality());
        author.setBirthDate(request.getBirthDate());
        author.setDeathDate(request.getDeathDate());

        Author savedAuthor = authorRepository.save(author);

        return toResponse(savedAuthor);
    }

    public AuthorResponse getAuthorById(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
        return toResponse(author);
    }

    // public AuthorResponse updateAuthor(UUID id, AuthorRequest authorRequest) {
    //     Author existingAuthor = authorRepository.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));

    //     existingAuthor.setName(authorRequest.getName());
    //     existingAuthor.setBiography(authorRequest.getBiography());
    //     existingAuthor.setNationality(authorRequest.getNationality());
    //     existingAuthor.setBirthDate(authorRequest.getBirthDate());
    //     existingAuthor.setDeathDate(authorRequest.getDeathDate());

    //     Author updatedAuthor = authorRepository.save(existingAuthor);
    //     return toResponse(updatedAuthor);
    // }

    public AuthorResponse update(UUID id, AuthorRequest request) {

        Author author = authorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Author not found with id: " + id)
                );

        if (request.getName() != null) {
            author.setName(request.getName());
        }

        if (request.getBiography() != null) {
            author.setBiography(request.getBiography());
        }

        if (request.getNationality() != null) {
            author.setNationality(request.getNationality());
        }

        if (request.getBirthDate() != null) {
            author.setBirthDate(request.getBirthDate());
        }

        if (request.getDeathDate() != null) {
            author.setDeathDate(request.getDeathDate());
        }

        Author updatedAuthor = authorRepository.save(author);

        return toResponse(updatedAuthor);
    }

    public AuthorResponse getAuthorByName(String name) {
        Author author = authorRepository.findAll().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Author not found with name: " + name));
        return toResponse(author);
    }

    public boolean deleteAllAuthors() {
        try {
            authorRepository.deleteAll();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private AuthorResponse toResponse(Author author) {

        AuthorResponse response = new AuthorResponse();

        response.setId(author.getId());
        response.setName(author.getName());
        response.setBiography(author.getBiography());
        response.setNationality(author.getNationality());
        response.setBirthDate(author.getBirthDate());
        response.setDeathDate(author.getDeathDate());
        response.setCreatedAt(author.getCreatedAt());
        response.setUpdatedAt(author.getUpdatedAt());

        return response;
    }

    public List<AuthorResponse> getAuthors() {
        return authorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

}
