package com.example.bookstorefull.controller;

import com.example.bookstorefull.dto.BookDto;
import com.example.bookstorefull.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/book")
public class BookController {

//    @PostMapping("/createBook")
//    public BookDto createBook(@RequestBody BookDto bookDto){
//        return bookService.createBook(bookDto);
//    }
}
