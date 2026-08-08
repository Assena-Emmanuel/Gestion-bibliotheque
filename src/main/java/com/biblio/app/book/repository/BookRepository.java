package com.biblio.app.book.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblio.app.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {
    boolean existsByTitle(String title);
}
