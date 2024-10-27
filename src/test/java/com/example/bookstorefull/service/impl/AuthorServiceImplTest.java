package com.example.bookstorefull.service.impl;

import com.example.bookstorefull.dto.AuthorDto;
import com.example.bookstorefull.dto.BookDto;
import com.example.bookstorefull.dto.SubscribedAuthorDto;
import com.example.bookstorefull.ecxception.AuthorNotFoundException;
import com.example.bookstorefull.ecxception.ErrorCode;
import com.example.bookstorefull.mapper.AuthorMapper;
import com.example.bookstorefull.model.Author;
import com.example.bookstorefull.model.Book;
import com.example.bookstorefull.model.Student;
import com.example.bookstorefull.repository.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private AuthorMapper authorMapper;
    @InjectMocks
    private AuthorServiceImpl service;

    private Author author;
    private AuthorDto authorDto;
    private Student student;
    private SubscribedAuthorDto subscribedAuthorDto;

    @BeforeEach
    void setUp() {
        authorDto = AuthorDto
                .builder()
                .age(25)
                .name("Robin Sharma")
                .books(List.of(BookDto
                        .builder()
                                .name("Ferrarisini satan rahib")
                        .build()))
                .build();
        author = Author
                .builder()
                .age(25)
                .name("Robin Sharma")
                .books(List.of(Book
                        .builder()
                        .name("Ferrarisini satan rahib")
                        .build()))
                .build();
        student = Student
                .builder()
                .name("Rauf Bayramov")
                .age(25)
                .subscribedStudents(List.of(Author.builder().name("Robin Sharma").build()))
                .currentlyReadingBooks(List.of(Book.builder().name("Ferrarisini satan rahib").build()))
                .build();
    }

    @AfterEach
    void tearDown() {
        authorDto = null;
    }

    @Test
    void createBook() {
        when(authorMapper.authorDtoToAuthor(authorDto)).thenReturn(author);
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(authorMapper.authorToAuthorDto(author)).thenReturn(authorDto);

        AuthorDto result = service.createBook(authorDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(authorDto.getName());
        assertThat(result.getAge()).isEqualTo(authorDto.getAge());
        assertThat(result.getBooks().size()).isEqualTo(authorDto.getBooks().size());
        assertThat(result.getBooks().get(0).getName()).isEqualTo(authorDto.getBooks().get(0).getName());


        // Verify interactions
        verify(authorMapper, times(1)).authorDtoToAuthor(authorDto);
        verify(authorRepository, times(1)).save(any(Author.class));
        verify(authorMapper, times(1)).authorToAuthorDto(author);

    }

    @Test
    void findByBook() {
        when(authorRepository.findByName(any(String.class))).thenReturn(Optional.of(author));
        when(authorMapper.authorToAuthorDto(author)).thenReturn(authorDto);

        var result = service.findByBook(anyString());

        assertThat(result).isNotNull();
        assertThat(result.getAge()).isEqualTo(authorDto.getAge());
        assertThat(result.getName()).isEqualTo(authorDto.getName());

        verify(authorRepository,times(1)).findByName(any(String.class));
        verify(authorMapper, times(1)).authorToAuthorDto(author);
    }
    @Test
    void testFindByBook_AuthorNotFoundException() {
        // Given
        String bookName = "NonExistentBook";
        when(authorRepository.findByName(bookName)).thenReturn(Optional.empty());

        // When & Then
        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class, () ->
                service.findByBook(bookName)
        );

        // Assert
        assertEquals(ErrorCode.AUTHOR_NOT_FOUND_0001, exception.getCode());
    }
    @Test
    void findById() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(authorMapper.authorToAuthorDto(author)).thenReturn(authorDto);

        var result = service.findById(anyLong());

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(authorDto.getName());
        assertThat(result.getAge()).isEqualTo(authorDto.getAge());

        verify(authorRepository, times(1)).findById(anyLong());
        verify(authorMapper, times(1)).authorToAuthorDto(author);
    }

    @Test
    void testFindById_AuthorNotFoundException() {
        // Given
        long bookId = 5l;
        when(authorRepository.findById(bookId)).thenReturn(Optional.empty());

        // When & Then
        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class, () ->
                service.findById(bookId)
        );

        // Assert
        assertEquals(ErrorCode.AUTHOR_NOT_FOUND_0001, exception.getCode());
    }

    @Test
    void deleteByBookName() {
        String bookName = "existingBookName";
        when(authorRepository.findByName(bookName)).thenReturn(Optional.of(author));
        doNothing().when(authorRepository).delete(author);

        service.deleteByBookName(bookName);

        verify(authorRepository, times(1)).findByName(bookName);
        verify(authorRepository,times(1)).delete(author);
    }

    @Test
    void testDeleteByBook_AuthorNotFoundException() {
        // Given
        String bookName = "NonExistentBook";
        when(authorRepository.findByName(bookName)).thenReturn(Optional.empty());

        // When & Then
        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class, () ->
                service.deleteByBookName(bookName)
        );

        // Assert
        assertEquals(ErrorCode.AUTHOR_NOT_FOUND_0001, exception.getCode());
    }
    @Test
    void updateAuthor() {
        long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(authorMapper.authorToAuthorDto(any(Author.class))).thenReturn(authorDto);

        var updatedAuthorDto = service.updateAuthor(authorId, authorDto);

        verify(authorRepository).findById(authorId);
        verify(modelMapper).map(authorDto, author);
        verify(authorRepository).save(author);
        verify(authorMapper).authorToAuthorDto(author);

        assertSame(updatedAuthorDto, authorDto);
    }

    @Test
    void testUpdateAuthor_AuthorNotFound() {
        long authorId = 1L;

        // Mock the behavior of authorRepository to return empty
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // Verify that AuthorNotFoundException is thrown
        AuthorNotFoundException thrown = assertThrows(AuthorNotFoundException.class, () -> {
            service.updateAuthor(authorId, authorDto);
        });

        // Verify the exception message and error code
        assertEquals(ErrorCode.AUTHOR_NOT_FOUND_0001, thrown.getCode());
    }
    @Test
    void findAll() {
        when(authorRepository.findAll()).thenReturn(Arrays.asList(author));
        when(modelMapper.map(author, AuthorDto.class)).thenReturn(authorDto);

        List<AuthorDto> authors = service.findAll();

        verify(authorRepository).findAll();
        verify(modelMapper).map(author, AuthorDto.class);

        assertEquals(1, authors.size());
        assertTrue(authors.contains(authorDto));
    }
    @Test
    void testFindAll_NoAuthors() {
        // Mock the behavior of authorRepository to return an empty list
        when(authorRepository.findAll()).thenReturn(Collections.emptyList());

        // Call the method
        List<AuthorDto> authors = service.findAll();

        // Verify the interaction
        verify(authorRepository).findAll();

        // Assert the result
        assertTrue(authors.isEmpty());
    }

    @Test
    void findAllByName() {
        String name = "existingName";
        when(authorRepository.findAllByName(name)).thenReturn(Arrays.asList(author));
        when(modelMapper.map(author,AuthorDto.class)).thenReturn(authorDto);
        List<AuthorDto>authorDtoList = service.findAllByName(name);
        verify(authorRepository).findAllByName(name);
        verify(modelMapper).map(author,AuthorDto.class);
        assertEquals(1,authorDtoList.size());
        assertTrue(authorDtoList.contains(authorDto));
    }
    @Test
    void testFindAllByName_NoAuthorsFound() {
        String name = "Unknown Author";

        // Mock the behavior of authorRepository to return an empty list
        when(authorRepository.findAllByName(name)).thenReturn(Collections.emptyList());

        // Call the method
        List<AuthorDto> authors = service.findAllByName(name);

        // Verify the interaction
        verify(authorRepository).findAllByName(name);

        // Assert the result
        assertTrue(authors.isEmpty());
    }

    @Test
    void findByBookWithAuthorName() {
        String authorName = "authorName";
        author.setBooks(Arrays.asList(
              Book.builder()
                      .name("book")
                      .build()
        ));
        when(authorRepository.findByBooks(authorName)).thenReturn(Arrays.asList(author));
        List<BookDto> result = service.findByBookWithAuthorName(authorName);
        assertEquals(1, result.size());
        assertEquals("book", result.get(0).getName());
        verify(authorRepository, times(1)).findByBooks(authorName);
    }

    @Test
    void subscribedSave() {
        author.setSubscribedStudents(List.of(student));

        // Mock the mappings
        when(authorMapper.subscribedAuthorDtoToAuthor(subscribedAuthorDto)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.authorToSubscribedAuthorDto(author)).thenReturn(subscribedAuthorDto);

        // When
        SubscribedAuthorDto result = service.subscribedSave(subscribedAuthorDto);

        // Then
        assertEquals(subscribedAuthorDto, result); // Check if the result matches the expected DTO

        // Verify interactions
        verify(authorMapper, times(1)).subscribedAuthorDtoToAuthor(subscribedAuthorDto);
        verify(authorRepository, times(1)).save(author);
        verify(authorMapper, times(1)).authorToSubscribedAuthorDto(author);

        // Check that each student is subscribed to the author
        for (Student student : author.getSubscribedStudents()) {
            assertEquals(List.of(author), student.getSubscribedStudents());
        }
    }
}