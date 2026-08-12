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
import com.biblio.app.common.constant.ApiMessages;

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
            throw new RuntimeException(ApiMessages.bookNotFoundWithId(id));
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
            throw new RuntimeException(ApiMessages.AUTHOR_NOT_FOUND);
        }
        if(book.getCategoryId() == null || !categorieService.existsById(book.getCategoryId())) {
            throw new RuntimeException(ApiMessages.CATEGORY_NOT_FOUND);
        }
        if(book.getAvailableCopies() != null && book.getTotalCopies() != null && book.getAvailableCopies() > book.getTotalCopies()) {
            throw new RuntimeException(ApiMessages.AVAILABLE_COPIES_EXCEED_TOTAL);
        }

        if(book.getAvailableCopies() < 0) {
            book.setAvailableCopies(0);
        }

        if(book.getTotalCopies() <= 0) {
            throw new RuntimeException(ApiMessages.TOTAL_COPIES_NEGATIVE);
        }

        if(existsByTitle(book.getTitle())){
            throw new RuntimeException(ApiMessages.BOOK_ALREADY_EXISTS);
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

    public BookResponse updateBook(UUID id, BookRequest book) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ApiMessages.bookNotFoundWithId(id)));

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
        if (book.getAuthorId() != null) {
            existingBook.setAuthor(authorService.getAuthorEntityById(book.getAuthorId()));
        }
        if (book.getCategoryId() != null) {
            existingBook.setCategory(categorieService.getCategorieEntityById(book.getCategoryId()));
        }

        Book updatedBook = bookRepository.save(existingBook);
        return toBookResponse(updatedBook);
    }
}
