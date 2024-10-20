package com.example.bookstorefull.service;

import com.example.bookstorefull.dto.CurrentlyStudentReadingDto;
import com.example.bookstorefull.dto.ResponseStudentReadingBookDto;
import com.example.bookstorefull.dto.StudentDto;

import java.util.List;

public interface StudentService {
    StudentDto createStudent(StudentDto studentDto);
    List<CurrentlyStudentReadingDto>findById(long studentId);

    List<StudentDto>findAll();

    List<ResponseStudentReadingBookDto>findByCurrentlyReadingBooks(String bookName);
}
