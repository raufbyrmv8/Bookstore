package com.example.bookstorefull.repository;

import com.example.bookstorefull.model.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @EntityGraph(attributePaths = "books")
    @Query("SELECT a FROM Author a JOIN a.books b WHERE b.name = :name")
    Optional<Author> findByName(@Param("name") String name);
    @EntityGraph(attributePaths = "books")
    Optional<Author> findById(long id);

    @EntityGraph(attributePaths = "books")
    List<Author> findAll();
    @EntityGraph(attributePaths = "books")
    @Query("SELECT a FROM Author a JOIN a.books WHERE a.name = :name")
    List<Author>findAllByName(@Param("name") String name);
    @EntityGraph(attributePaths = "books")
    @Query("SELECT a from Author a join a.books b where a.name = :authorName")
    List<Author>findByBooks(@Param("authorName") String authorName);
}
