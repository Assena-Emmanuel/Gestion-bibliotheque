package com.biblio.app.book.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.biblio.app.author.service.AuthorService;
import com.biblio.app.book.dto.BookRequest;
import com.biblio.app.book.dto.BookResponse;
import com.biblio.app.book.entity.Book;
import com.biblio.app.book.repository.BookRepository;
import com.biblio.app.categories.service.CategorieService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategorieService categorieService;

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .publicationYear(book.getPublicationYear())
                .availableCopies(book.getAvailableCopies())
                .totalCopies(book.getTotalCopies())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .language(book.getLanguage())
                .author(book.getAuthor() != null ? authorService.getAuthorById(book.getAuthor().getId()) : null)
                .category(book.getCategory() != null ? categorieService.getCategorieById(book.getCategory().getId()).orElse(null): null)
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::toBookResponse)
                .toList();
    }

    public Optional<BookResponse> getBookById(UUID id) {
        return bookRepository.findById(id).map(this::toBookResponse);
    }

    public void deleteBookById(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public boolean existsById(UUID id) {
        return bookRepository.existsById(id);   
    }

    public boolean existsByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }

    public Optional<BookResponse> createBook(BookRequest book) {

        if(book.getAuthorId() == null || !authorService.existsById(book.getAuthorId())) {
            throw new RuntimeException("Author not found ");
        }
        if(book.getCategoryId() == null || !categorieService.existsById(book.getCategoryId())) {
            throw new RuntimeException("Category not found");
        }
        if(book.getAvailableCopies() != null && book.getTotalCopies() != null && book.getAvailableCopies() > book.getTotalCopies()) {
            throw new RuntimeException("Available copies cannot be greater than total copies");
        }

        if(book.getAvailableCopies() < 0) {
            book.setAvailableCopies(0);
        }

        if(book.getTotalCopies() <= 0) {
            throw new RuntimeException("Total copies cannot be negative");
        }

        Book newBook = new Book();
        newBook.setTitle(book.getTitle());
        newBook.setDescription(book.getDescription());
        newBook.setPublicationYear(book.getPublicationYear());
        newBook.setAvailableCopies(book.getAvailableCopies());
        newBook.setTotalCopies(book.getTotalCopies());
        newBook.setIsbn(book.getIsbn());
        newBook.setPublisher(book.getPublisher());
        newBook.setLanguage(book.getLanguage());
        newBook.setAuthor(authorService.getAuthorEntityById(book.getAuthorId()));
        newBook.setCategory(categorieService.getCategorieEntityById(book.getCategoryId()));
        
        Book savedBook = bookRepository.save(newBook);
        return Optional.of(toBookResponse(savedBook));
    }

    public BookResponse updateBook(UUID id, Book book) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        if (book.getTitle() != null && !book.getTitle().isEmpty()) {
            existingBook.setTitle(book.getTitle());
        }
        if (book.getDescription() != null && !book.getDescription().isEmpty()) {
            existingBook.setDescription(book.getDescription());
        }
        if (book.getPublicationYear() != null) {
            existingBook.setPublicationYear(book.getPublicationYear());
        }
        if (book.getAvailableCopies() != null && book.getAvailableCopies() >= 0 && book.getAvailableCopies() <= existingBook.getTotalCopies()) {
            existingBook.setAvailableCopies(book.getAvailableCopies());
        }
        if (book.getTotalCopies() != null && book.getTotalCopies() > 0) {
            existingBook.setTotalCopies(book.getTotalCopies());
        }
        if (book.getIsbn() != null && !book.getIsbn().isEmpty()) {
            existingBook.setIsbn(book.getIsbn());
        }
        if (book.getPublisher() != null && !book.getPublisher().isEmpty()) {
            existingBook.setPublisher(book.getPublisher());
        }
        if (book.getLanguage() != null && !book.getLanguage().isEmpty()) {
            existingBook.setLanguage(book.getLanguage());
        }
        if (book.getAuthor() != null) {
            existingBook.setAuthor(book.getAuthor());
        }
        if (book.getCategory() != null) {
            existingBook.setCategory(book.getCategory());
        }

        Book updatedBook = bookRepository.save(existingBook);
        return toBookResponse(updatedBook);
    }
}
