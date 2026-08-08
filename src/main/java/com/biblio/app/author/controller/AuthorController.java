package com.biblio.app.author.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblio.app.author.dto.AuthorRequest;
import com.biblio.app.author.dto.AuthorResponse;
import com.biblio.app.author.service.AuthorService;
import com.biblio.app.common.response.ResponseDto;

import lombok.AllArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@AllArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseDto getAuthors() {
        return new ResponseDto(
            true, 
            "Authors retrieved successfully", 
            authorService.getAuthors());
        
    }

    @GetMapping("/{id}")
    public ResponseDto getAuthor(@PathVariable UUID id) {
        return new ResponseDto(
            true,
            "Author retrieved successfully",
            authorService.getAuthorById(id)
        );
    }

    @PostMapping
    public ResponseDto postAuthor(@RequestBody AuthorRequest authorRequest) {
        return new ResponseDto(
            true,
            "Author created successfully",
            authorService.createAuthor(authorRequest)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorResponse> update(
            @PathVariable UUID id,
            @RequestBody AuthorRequest request) {

        AuthorResponse response = authorService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(
            @PathVariable UUID id,
            @RequestBody AuthorRequest request) {

        AuthorResponse response = authorService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseDto deleteAuthor(@PathVariable UUID id) {
        if (!authorService.existsById(id)) {
            return new ResponseDto(
                false,
                "Author not found with id: " + id,
                null
            );
        }
        authorService.deleteAuthorById(id);
        return new ResponseDto(
            true,
            "Author deleted successfully",
            null
        );
    }

    @DeleteMapping
    public ResponseDto deleteAllAuthors() {
        authorService.deleteAllAuthors();
        return new ResponseDto(
            true,
            "All authors deleted successfully",
            null
        );
    }
}