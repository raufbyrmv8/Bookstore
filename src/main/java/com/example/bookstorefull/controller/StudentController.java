package com.example.bookstorefull.controller;

import com.example.bookstorefull.dto.CurrentlyStudentReadingDto;
import com.example.bookstorefull.dto.ResponseStudentReadingBookDto;
import com.example.bookstorefull.dto.StudentDto;
import com.example.bookstorefull.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/createStudent")
    public StudentDto createStudent (@RequestBody StudentDto studentDto){
        return studentService.createStudent(studentDto);
    }

    @GetMapping("/currentlyReadingBooks/{id}")
    public List<CurrentlyStudentReadingDto> currentlyReadingBooks(@PathVariable long id){
        return studentService.findById(id);
    }

    @GetMapping("/find-all")
    public List<StudentDto>findAll(){
        return studentService.findAll();
    }

    @GetMapping("/{bookName}")
    public List<ResponseStudentReadingBookDto> getStudentsWithSpecificBook(@PathVariable String bookName){
        return studentService.findByCurrentlyReadingBooks(bookName);
    }


}
