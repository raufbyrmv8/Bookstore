package com.example.bookstorefull.controller;

import com.example.bookstorefull.dto.AuthorDto;
import com.example.bookstorefull.dto.BookDto;
import com.example.bookstorefull.dto.SubscribedAuthorDto;
import com.example.bookstorefull.service.AuthorService;
import com.example.bookstorefull.service.BookPublishingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final BookPublishingService bookPublishingService;

    @PostMapping("/createBook")
    public AuthorDto createBook(@RequestBody AuthorDto authorDto){
        return authorService.createBook(authorDto);
    }

    @GetMapping("/findBook/{name}")
    public AuthorDto findByBook(@PathVariable String name){
        return authorService.findByBook(name);
    }

    @DeleteMapping("/delete/{name}")
    public void deleteByBook(@PathVariable String name){
        authorService.deleteByBookName(name);
    }

    @PutMapping("/updateAuthor/{id}")
    public AuthorDto updateAuthor(@PathVariable long id, @RequestBody AuthorDto authorDto){
        return authorService.updateAuthor(id,authorDto);
    }

    @GetMapping("/get/{id}")
    public AuthorDto findAuthorById(@PathVariable long id){
        return authorService.findById(id);
    }

    @GetMapping("/find-all")
    public List<AuthorDto> findAll(){
        return authorService.findAll();
    }
    @GetMapping("/find-all/{name}")
    public List<AuthorDto> findAllByName(@PathVariable String name){
        return authorService.findAllByName(name);
    }

    @GetMapping("/{authorName}")
    public List<BookDto>findByBookWithAuthorName(@PathVariable String authorName){
        return authorService.findByBookWithAuthorName(authorName);
    }

    @PostMapping("/subscribedSave")
    public SubscribedAuthorDto subscribedSave(@RequestBody SubscribedAuthorDto subscribedAuthorDto){
        return authorService.subscribedSave(subscribedAuthorDto);
    }
    @PostMapping("/{authorId}/publish")
    public void publishBook(@PathVariable long authorId, @RequestBody BookDto bookDto){
        bookPublishingService.publishBook(authorId, bookDto);
    }
}
