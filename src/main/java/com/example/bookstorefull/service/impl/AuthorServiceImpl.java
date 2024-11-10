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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final CacheManager cacheManager;
    private final AuthorMapper authorMapper;
    private final ModelMapper modelMapper;
    @Override
    public AuthorDto createBook(AuthorDto authorDto) {
        Author author = authorMapper.authorDtoToAuthor(authorDto);
        author.getBooks()
                .forEach(book -> book.setAuthor(author));
        List<Book>books = author.getBooks();
        author.setBooks(books);
        return authorMapper.authorToAuthorDto(authorRepository.save(author));
    }

    @Override
    public AuthorDto findByBook(String name) {
        Cache cache = cacheManager.getCache("authors");
        if (cache != null) {
            // Try to get from cache
            Author cachedAuthor = cache.get(name, Author.class);
            if (cachedAuthor != null) {
                log.info("Cache hit for author: {}", name);
                return authorMapper.authorToAuthorDto(cachedAuthor);
            }
        }

        Author author = authorRepository.findByName(name)
                .orElseThrow(() -> new AuthorNotFoundException(ErrorCode.AUTHOR_NOT_FOUND_0001, name));

        if (cache != null) {
            cache.put(name, author); // Add to cache
        }
        return authorMapper.authorToAuthorDto(author);
    }

    @Override
    public AuthorDto findById(long findById) {
        Cache cache = cacheManager.getCache("authors");
        if (cache != null){
            Author cachedAuthor = cache.get(findById, Author.class);
            if (cachedAuthor != null){
                log.info("Cache hit for author: {}", findById);
                return authorMapper.authorToAuthorDto(cachedAuthor);
            }
        }
        Author author = authorRepository.findById(findById)
                .orElseThrow(() -> new AuthorNotFoundException(ErrorCode.AUTHOR_NOT_FOUND_0001, String.valueOf(findById)));
        if (cache != null){
            cache.put(findById, author);
        }
        return authorMapper.authorToAuthorDto(author);
    }

    @Override
    public void deleteByBookName(String name) {
        Author author = authorRepository.findByName(name)
                .orElseThrow(() -> new AuthorNotFoundException(ErrorCode.AUTHOR_NOT_FOUND_0001, name));
        authorRepository.delete(author);
        Cache cache = cacheManager.getCache("authors");
        if (cache != null) {
            cache.evict(name); // Remove by name
            cache.evict(author.getId()); // Remove by ID, if cached by ID as well
        }

        log.info("Deleted author with name: {}", name);
    }

    @Override
    public AuthorDto updateAuthor(long id,AuthorDto authorDto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(ErrorCode.AUTHOR_NOT_FOUND_0001, String.valueOf(id)));
        modelMapper.map(authorDto,author);
        return authorMapper.authorToAuthorDto(authorRepository.save(author));
    }

    @Override
    public List<AuthorDto> findAll() {
        Cache cache = cacheManager.getCache("authors");

        // Check if "allAuthors" is cached
        if (cache != null) {
            List<AuthorDto> cachedAuthors = cache.get("allAuthors", List.class);
            if (cachedAuthors != null) {
                log.info("Cache hit for all authors");
                return cachedAuthors;
            }
        }
        List<Author> authors = authorRepository.findAll();
        List<AuthorDto> authorDtos = authors.stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());

        // Add the result to the cache
        if (cache != null) {
            cache.put("allAuthors", authorDtos);
        }

        return authorDtos;
    }

    @Override
    public List<AuthorDto> findAllByName(String name) {
        Cache cache = cacheManager.getCache("authors");

        // Try to get authors by name from cache
        if (cache != null) {
            List<AuthorDto> cachedAuthorsByName = cache.get(name, List.class);
            if (cachedAuthorsByName != null) {
                log.info("Cache hit for authors by name: {}", name);
                return cachedAuthorsByName;
            }
        }
        List<Author> authors = authorRepository.findAllByName(name);
        List<AuthorDto> authorDtos = authors.stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());

        // Cache the result
        if (cache != null) {
            cache.put(name, authorDtos);
        }

        return authorDtos;
    }

    @Override
    public List<BookDto> findByBookWithAuthorName(String authorName) {
        Cache cache = cacheManager.getCache("books");

        // Check for cached books by author name
        if (cache != null) {
            List<BookDto> cachedBooks = cache.get(authorName, List.class);
            if (cachedBooks != null) {
                log.info("Cache hit for books by author name: {}", authorName);
                return cachedBooks;
            }
        }

        // Fetch from database if not cached
        List<Author> authors = authorRepository.findByBooks(authorName);
        List<BookDto> bookDtos = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .map(book -> new BookDto(book.getName()))
                .collect(Collectors.toList());

        // Cache the result
        if (cache != null) {
            cache.put(authorName, bookDtos);
        }

        return bookDtos;
    }
    @Override
    public SubscribedAuthorDto subscribedSave(SubscribedAuthorDto subscribedAuthorDto) {
        Author author = authorMapper.subscribedAuthorDtoToAuthor(subscribedAuthorDto);
        author.getSubscribedStudents()
                .forEach(student -> student.setSubscribedStudents(List.of(author)));

        List<Student> studentList = author.getSubscribedStudents();
        author.setSubscribedStudents(studentList);
        Author savedAuthor = authorRepository.save(author);

        // Add to cache
        Cache cache = cacheManager.getCache("subscribedAuthors");
        if (cache != null) {
            cache.put(savedAuthor.getId(), authorMapper.authorToSubscribedAuthorDto(savedAuthor));
        }

        return authorMapper.authorToSubscribedAuthorDto(savedAuthor);
    }
}
