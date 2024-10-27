package com.example.bookstorefull.service.impl;

import com.example.bookstorefull.dto.CurrentlyStudentReadingDto;
import com.example.bookstorefull.dto.ResponseStudentReadingBookDto;
import com.example.bookstorefull.dto.StudentDto;
import com.example.bookstorefull.ecxception.ErrorCode;
import com.example.bookstorefull.ecxception.StudentNotFoundException;
import com.example.bookstorefull.mapper.StudentMapper;
import com.example.bookstorefull.model.Book;
import com.example.bookstorefull.model.Student;
import com.example.bookstorefull.repository.StudentRepository;
import com.example.bookstorefull.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final ModelMapper modelMapper;
    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        Student student = studentMapper.studentDtotoStudent(studentDto);
        student.getCurrentlyReadingBooks()
                .forEach(book -> book.setStudents(List.of(student)));
        List<Book>bookList = student.getCurrentlyReadingBooks();
        student.setCurrentlyReadingBooks(bookList);
        return studentMapper.studentToStudentDto(studentRepository.save(student));
    }

    @Override
    public List<CurrentlyStudentReadingDto> findById(long studentId) {
        List<Student> students = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(ErrorCode.STUDENT_NOT_FOUND_0001, String.valueOf(studentId)));
        return students.stream()
                .map(bookRes -> modelMapper.map(bookRes, CurrentlyStudentReadingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDto> findAll() {
        List<Student> student = studentRepository.findAll();
        return student.stream()
                .map(studentRes -> modelMapper.map(studentRes, StudentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseStudentReadingBookDto> findByCurrentlyReadingBooks(String bookName) {
        List<Student> students = studentRepository.findByCurrentlyReadingBooks(bookName)
                .orElseThrow(() -> new StudentNotFoundException(ErrorCode.STUDENT_NOT_FOUND_0001, bookName));
        return students.stream()
                .map(student -> modelMapper.map(student, ResponseStudentReadingBookDto.class))
                .collect(Collectors.toList());
    }
}
