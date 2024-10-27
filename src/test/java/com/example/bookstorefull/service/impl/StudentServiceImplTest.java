package com.example.bookstorefull.service.impl;

import com.example.bookstorefull.dto.BookDto;
import com.example.bookstorefull.dto.CurrentlyStudentReadingDto;
import com.example.bookstorefull.dto.ResponseStudentReadingBookDto;
import com.example.bookstorefull.dto.StudentDto;
import com.example.bookstorefull.ecxception.AuthorNotFoundException;
import com.example.bookstorefull.ecxception.ErrorCode;
import com.example.bookstorefull.ecxception.StudentNotFoundException;
import com.example.bookstorefull.mapper.StudentMapper;
import com.example.bookstorefull.model.Author;
import com.example.bookstorefull.model.Book;
import com.example.bookstorefull.model.Student;
import com.example.bookstorefull.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentMapper studentMapper;
    @InjectMocks
    private StudentServiceImpl service;

    private Student student;
    private StudentDto studentDto;
    private CurrentlyStudentReadingDto readingDto;
    private ResponseStudentReadingBookDto responseStudentReadingBookDto;

    @BeforeEach
    void setUp() {
        studentDto = StudentDto
                .builder()
                .age(25)
                .name("Rauf Bayramov")
                .currentlyReadingBooks(List.of(BookDto
                        .builder()
                                .name("Ferrarisini satan rahib")
                        .build()))
                .build();
        student = Student
                .builder()
                .name("Rauf Bayramov")
                .age(25)
                .subscribedStudents(List.of(Author.builder().name("Robin Sharma").age(25).name("Rauf Bayramov").build()))
                .currentlyReadingBooks(List.of(Book.builder().name("Ferrarisini satan rahib").author(Author.builder().name("Robin Sharma").build()).build()))
                .build();
        readingDto = CurrentlyStudentReadingDto
                .builder()
                .currentlyReadingBooks(List.of(BookDto.builder().name("Ferrarisini satan rahib").build()))
                .build();
        responseStudentReadingBookDto = ResponseStudentReadingBookDto
                .builder()
                .age(25)
                .name("Ferrarisini satan rahib")
                .build();
    }

    @AfterEach
    void tearDown() {
        studentDto = null;
        student = null;
        readingDto = null;
        responseStudentReadingBookDto = null;
    }

    @Test
    void createStudent() {
        when(studentMapper.studentDtotoStudent(studentDto)).thenReturn(student);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentMapper.studentToStudentDto(student)).thenReturn(studentDto);

        var result = service.createStudent(studentDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(studentDto.getName());
        assertThat(result.getAge()).isEqualTo(studentDto.getAge());

        verify(studentMapper, times(1)).studentDtotoStudent(studentDto);
        verify(studentRepository, times(1)).save(student);
        verify(studentMapper, times(1)).studentToStudentDto(student);
    }

    @Test
    void findById() {
        long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(List.of(student)));
        when(modelMapper.map(student,CurrentlyStudentReadingDto.class)).thenReturn(readingDto);

        List<CurrentlyStudentReadingDto> result = service.findById(studentId);

        assertEquals(1, result.size());
        assertEquals(readingDto, result.get(0));

        verify(studentRepository, times(1)).findById(studentId);
        verify(modelMapper,times(1)).map(student, CurrentlyStudentReadingDto.class);
    }
    @Test
    void testFindById_StudentNotFoundException() {
        // Given
        long studentId = 5l;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When & Then
        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () ->
                service.findById(studentId)
        );

        // Assert
        assertEquals(ErrorCode.STUDENT_NOT_FOUND_0001, exception.getCode());
    }


    @Test
    void findAll() {
        when(studentRepository.findAll()).thenReturn(List.of(student));
        when(modelMapper.map(student, StudentDto.class)).thenReturn(studentDto);

        var result = service.findAll();

        assertThat(result).isNotNull();
        assertEquals(1, result.size());

        verify(studentRepository,times(1)).findAll();
        verify(modelMapper,times(1)).map(student, StudentDto.class);
    }

    @Test
    void findByCurrentlyReadingBooks() {
        String name = "bookName";
        when(studentRepository.findByCurrentlyReadingBooks(name)).thenReturn(Optional.of(List.of(student)));
        when(modelMapper.map(student, ResponseStudentReadingBookDto.class)).thenReturn(responseStudentReadingBookDto);

        var result = service.findByCurrentlyReadingBooks(name);

        assertThat(result).isNotNull();
        assertEquals(1, result.size());

        verify(studentRepository,times(1)).findByCurrentlyReadingBooks(name);
        verify(modelMapper,times(1)).map(student, ResponseStudentReadingBookDto.class);
    }
    @Test
    void testFindByCurrentlyReadingBooks_StudentNotFoundException() {
        // Given
        String bookName = "NonExistentBook";
        when(studentRepository.findByCurrentlyReadingBooks(bookName)).thenReturn(Optional.empty());

        // When & Then
        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () ->
                service.findByCurrentlyReadingBooks(bookName)
        );

        // Assert
        assertEquals(ErrorCode.STUDENT_NOT_FOUND_0001, exception.getCode());
    }
}