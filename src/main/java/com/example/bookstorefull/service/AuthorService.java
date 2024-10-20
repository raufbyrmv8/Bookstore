package com.example.bookstorefull.service;

import com.example.bookstorefull.dto.AuthorDto;
import com.example.bookstorefull.dto.BookDto;
import com.example.bookstorefull.dto.StudentDto;
import com.example.bookstorefull.dto.SubscribedAuthorDto;

import java.util.List;

public interface AuthorService {
    AuthorDto createBook(AuthorDto authorDto);
    AuthorDto findByBook(String name);
    AuthorDto findById(long findById);
    void deleteByBookName(String name);
    AuthorDto updateAuthor(long id, AuthorDto authorDto);
    List<AuthorDto>findAll();
    List<AuthorDto>findAllByName(String name);
    List<BookDto>findByBookWithAuthorName(String authorName);
    SubscribedAuthorDto subscribedSave(SubscribedAuthorDto subscribedAuthorDto);

}
