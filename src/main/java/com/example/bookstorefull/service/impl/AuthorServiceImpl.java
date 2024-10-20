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
import com.example.bookstorefull.repository.StudentRepository;
import com.example.bookstorefull.service.AuthorService;
import com.example.bookstorefull.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final ModelMapper modelMapper;
    @Override
    public AuthorDto createBook(AuthorDto authorDto) {
        Author author = authorMapper.INSTANCE.authorDtoToAuthor(authorDto);
        author.getBooks()
                .forEach(book -> book.setAuthor(author));
        List<Book>books = author.getBooks();
        author.setBooks(books);
        return authorMapper.INSTANCE.authorToAuthorDto(authorRepository.save(author));
    }

    @Override
    public AuthorDto findByBook(String name) {
        Author author = authorRepository.findByName(name)
                .orElseThrow(() -> new AuthorNotFoundException(ErrorCode.AUTHOR_NOT_FOUND_0001, name));
        return authorMapper.INSTANCE.authorToAuthorDto(author);
    }

    @Override
    public AuthorDto findById(long findById) {
        Author author = authorRepository.findById(findById)
                .orElseThrow(() -> new AuthorNotFoundException(ErrorCode.AUTHOR_NOT_FOUND_0001, String.valueOf(findById)));
        return authorMapper.INSTANCE.authorToAuthorDto(author);
    }

    @Override
    public void deleteByBookName(String name) {
        Author author = authorRepository.findByName(name)
                .orElseThrow(() -> new AuthorNotFoundException(ErrorCode.AUTHOR_NOT_FOUND_0001, name));
        authorRepository.delete(author);
    }

    @Override
    public AuthorDto updateAuthor(long id,AuthorDto authorDto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(ErrorCode.AUTHOR_NOT_FOUND_0001, String.valueOf(id)));
        modelMapper.map(authorDto,author);
        return authorMapper.INSTANCE.authorToAuthorDto(authorRepository.save(author));
    }

    @Override
    public List<AuthorDto> findAll() {
        List<Author> author = authorRepository.findAll();
        return author.stream()
                .map(authorDto -> modelMapper.map(authorDto,AuthorDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorDto> findAllByName(String name) {
        List<Author> author = authorRepository.findAllByName(name);
        return author.stream()
                .map(authorRes -> modelMapper.map(authorRes, AuthorDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDto> findByBookWithAuthorName(String authorName) {
        List<Author>authors = authorRepository.findByBooks(authorName);
        return authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .map(book -> new BookDto(book.getName()))
                .collect(Collectors.toList());
    }
    @Override
    public SubscribedAuthorDto subscribedSave(SubscribedAuthorDto subscribedAuthorDto) {
        Author author = authorMapper.INSTANCE.subscribedAuthorDtoToAuthor(subscribedAuthorDto);
        author.getSubscribedStudents()
                .forEach(student -> student.setSubscribedStudents(List.of(author)));
        List<Student>studentList = author.getSubscribedStudents();
        author.setSubscribedStudents(studentList);
        return authorMapper.INSTANCE.authorToSubscribedAuthorDto(authorRepository.save(author));
    }
}
