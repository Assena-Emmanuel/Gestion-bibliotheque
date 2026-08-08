package com.biblio.app.book.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblio.app.book.dto.BookRequest;
import com.biblio.app.book.service.BookService;
import com.biblio.app.common.response.ResponseDto;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseDto getBooks() {
        return new ResponseDto(
            true, 
            "Books retrieved successfully", 
            bookService.getAllBooks());
        
    }

    @PostMapping
    public ResponseDto postBook(@RequestBody BookRequest book) {
        if(bookService.existsByTitle(book.getTitle())) {
            return new ResponseDto(
                false,
                "Book already exists with title: " + book.getTitle(),
                null
            );
        }
        
        return new ResponseDto(
            true,
            "Book created successfully",
            bookService.createBook(book)
        );
    }

    @DeleteMapping("{id}")
    public ResponseDto deleteBook(@PathVariable UUID id){
        bookService.deleteBookById(id);
        return new ResponseDto(
            true,
            "Book already deleted",
            null
        );
    }

}
