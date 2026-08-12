package com.biblio.app.author.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblio.app.author.dto.AuthorRequest;
import com.biblio.app.author.dto.AuthorResponse;
import com.biblio.app.author.service.AuthorService;
import com.biblio.app.common.response.ResponseDto;
import com.biblio.app.common.constant.ApiMessages;

import lombok.AllArgsConstructor;

import java.util.List;
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
            ApiMessages.AUTHORS_RETRIEVED,
            authorService.getAuthors());
        
    }

    @GetMapping("/{id}")
    public ResponseDto getAuthor(@PathVariable UUID id) {
        return new ResponseDto(
            true,
            ApiMessages.AUTHOR_RETRIEVED,
            authorService.getAuthorById(id)
        );
    }

    @PostMapping
    public ResponseDto postAuthor(@RequestBody AuthorRequest authorRequest) {
        return new ResponseDto(
            true,
            ApiMessages.AUTHOR_CREATED,
            authorService.createAuthor(authorRequest)
        );
    }

    @PostMapping("/more")
    public ResponseDto postMoreAuthors(@RequestBody List<AuthorRequest> param) {
        return new ResponseDto(
            true,
            ApiMessages.AUTHORS_CREATED,
            authorService.createAuthors(param)
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
                ApiMessages.authorNotFoundWithId(id),
                null
            );
        }
        authorService.deleteAuthorById(id);
        return new ResponseDto(
            true,
            ApiMessages.AUTHOR_DELETED,
            null
        );
    }

    @DeleteMapping
    public ResponseDto deleteAllAuthors() {
        authorService.deleteAllAuthors();
        return new ResponseDto(
            true,
            ApiMessages.ALL_AUTHORS_DELETED,
            null
        );
    }
}
