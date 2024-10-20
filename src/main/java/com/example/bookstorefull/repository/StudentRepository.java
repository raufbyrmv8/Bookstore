package com.example.bookstorefull.repository;
import com.example.bookstorefull.dto.ResponseStudentReadingBookDto;
import com.example.bookstorefull.model.Author;
import com.example.bookstorefull.model.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @EntityGraph(attributePaths = "currentlyReadingBooks")
    Optional<List<Student>>findById(long studentId);

    @EntityGraph(attributePaths = "currentlyReadingBooks")
    List<Student>findAll();
    @EntityGraph(attributePaths = "currentlyReadingBooks")
    @Query("select s from Student s join s.currentlyReadingBooks c where c.name = :bookName")
    Optional<List<Student>>findByCurrentlyReadingBooks(@Param("bookName") String bookName);
}
