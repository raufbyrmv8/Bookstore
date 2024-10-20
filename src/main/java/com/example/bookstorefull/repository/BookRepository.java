package com.example.bookstorefull.repository;

import com.example.bookstorefull.dto.BookDto;
import com.example.bookstorefull.model.Author;
import com.example.bookstorefull.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = {"students","author"})
    Book findByName(String name);
}
