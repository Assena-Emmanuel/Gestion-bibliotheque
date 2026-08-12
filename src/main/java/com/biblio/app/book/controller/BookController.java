package com.biblio.app.book.controller;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblio.app.book.dto.BookRequest;
import com.biblio.app.book.service.BookService;
import com.biblio.app.common.response.ResponseDto;
import com.biblio.app.common.constant.ApiMessages;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
@Tag(
    name="Books",
    description = "Gestion des livres de la bibliothèque"
)
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(
        summary = "Récupérer tous les livres",
        description = "Retourne la liste de tous les livres enregistrés dans la bibliothèque"
    )
    public ResponseDto getBooks() {
        return new ResponseDto(
            true, 
            ApiMessages.BOOKS_RETRIEVED,
            bookService.getAllBooks());
        
    }

    @GetMapping("{id}")
    public ResponseDto getMethodName(@PathVariable UUID id) {
        return new ResponseDto(
            true,
            ApiMessages.BOOK_RETRIEVED,
            bookService.getBookById(id)
        );
    }
    

    @PostMapping
    public ResponseEntity<ResponseDto> postBook(@RequestBody BookRequest book) {
       
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                new ResponseDto(
            true,
            ApiMessages.BOOK_CREATED,
            bookService.createBook(book)
            )
        );
    }

    @DeleteMapping("{id}")
    public ResponseDto deleteBook(@PathVariable UUID id){
        bookService.deleteBookById(id);
        return new ResponseDto(
            true,
            ApiMessages.BOOK_DELETED,
            null
        );
    }

    @PatchMapping("{id}")
    public ResponseDto updateBook(@PathVariable UUID id, @RequestBody BookRequest request){
        return new ResponseDto(
            true,
            ApiMessages.BOOK_UPDATED,
            bookService.updateBook(id, request)
        );
    }

}
