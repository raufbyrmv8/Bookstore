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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final ModelMapper modelMapper;
    private final CacheManager cacheManager;
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
        Cache cache = cacheManager.getCache("studentReadingBook");

        if (cache != null){
            List<CurrentlyStudentReadingDto>cachedStudentReading = cache.get("readingBook", List.class);
            if (cachedStudentReading != null){
                log.info("Cache hit for all cachedStudentReading");
                return cachedStudentReading;
            }
        }
        List<Student> students = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(ErrorCode.STUDENT_NOT_FOUND_0001, String.valueOf(studentId)));
        List<CurrentlyStudentReadingDto> currentlyStudentReadingDtos = students.stream()
                .map(bookRes -> modelMapper.map(bookRes, CurrentlyStudentReadingDto.class))
                .collect(Collectors.toList());

        if (cache != null){
            cache.put("readingBook", currentlyStudentReadingDtos);
        }
        return currentlyStudentReadingDtos;
    }

    @Override
    public List<StudentDto> findAll() {
        Cache cache = cacheManager.getCache("students");
        if (cache != null){
            List<StudentDto>cachedStudent = cache.get("studentsAll", List.class);
            if (cachedStudent != null){
                log.info("Cache hit for all students");
                return cachedStudent;
            }
        }
        List<Student> student = studentRepository.findAll();
        List<StudentDto>studentDtoList =  student.stream()
                .map(studentRes -> modelMapper.map(studentRes, StudentDto.class))
                .collect(Collectors.toList());

        if (cache != null){
            cache.put("studentsAll",studentDtoList);
        }
        return studentDtoList;
    }

    @Override
    public List<ResponseStudentReadingBookDto> findByCurrentlyReadingBooks(String bookName) {
        Cache cache = cacheManager.getCache("readingBooks");

        if (cache != null){
            List<ResponseStudentReadingBookDto> cacheReadingBook = cache.get("readingBooks", List.class);
            if (cacheReadingBook != null){
                log.info("Cache hit for all students");
                return cacheReadingBook;
            }
        }
        List<Student> students = studentRepository.findByCurrentlyReadingBooks(bookName)
                .orElseThrow(() -> new StudentNotFoundException(ErrorCode.STUDENT_NOT_FOUND_0001, bookName));
        List<ResponseStudentReadingBookDto> studentReadingBookDtos = students.stream()
                .map(student -> modelMapper.map(student, ResponseStudentReadingBookDto.class))
                .collect(Collectors.toList());

        if (cache != null){
            cache.put("readingBooks", studentReadingBookDtos);
        }
        return studentReadingBookDtos;
    }
}
