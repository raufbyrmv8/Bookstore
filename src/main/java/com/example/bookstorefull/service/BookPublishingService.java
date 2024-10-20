package com.example.bookstorefull.service;

import com.example.bookstorefull.dto.BookDto;
import com.example.bookstorefull.dto.MailAuthorDto;
import com.example.bookstorefull.dto.MailStudentDto;
import com.example.bookstorefull.model.Author;
import com.example.bookstorefull.model.Book;
import com.example.bookstorefull.model.Student;
import com.example.bookstorefull.repository.AuthorRepository;
import com.example.bookstorefull.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookPublishingService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;

    @Transactional
    public void publishBook(long authorId, BookDto bookDto){
        Author author = authorRepository.findById(authorId).orElseThrow(()->new RuntimeException("Author not found! "));
        Book book = modelMapper.map(bookDto, Book.class);
        Book bookRes = bookRepository.save(book);
        BookDto bookDtoRes = modelMapper.map(bookRes, BookDto.class);
        MailAuthorDto authorDto = modelMapper.map(author, MailAuthorDto.class);
        List<Student>studentList = author.getSubscribedStudents();

        for (Student student : studentList){
            MailStudentDto studentDto = modelMapper.map(student, MailStudentDto.class);
            notificationService.sendBookPublishedNotification(studentDto, bookDtoRes, authorDto);
        }
    }
}
