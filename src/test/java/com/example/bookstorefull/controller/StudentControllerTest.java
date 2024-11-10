package com.example.bookstorefull.controller;

import com.example.bookstorefull.dto.BookDto;
import com.example.bookstorefull.dto.CurrentlyStudentReadingDto;
import com.example.bookstorefull.dto.ResponseStudentReadingBookDto;
import com.example.bookstorefull.dto.StudentDto;
import com.example.bookstorefull.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;
    private StudentDto studentDto;
    private BookDto bookDto;
    private ResponseStudentReadingBookDto studentReadingBookDto;
    private CurrentlyStudentReadingDto currentlyStudentReadingDto;

    @BeforeEach
    void setUp() {
        studentDto = StudentDto
                .builder()
                .name("Rauf Bayramov")
                .age(25)
                .build();
        studentReadingBookDto = ResponseStudentReadingBookDto
                .builder()
                .name("Rauf Bayramov")
                .age(25)
                .build();
        bookDto = BookDto
                .builder()
                .name("Ferrarisini satan rahib")
                .build();
        currentlyStudentReadingDto = CurrentlyStudentReadingDto
                .builder()
                .currentlyReadingBooks(List.of(bookDto))
                .build();
    }

    @AfterEach
    void tearDown() {
        studentDto = null;
        bookDto = null;
        currentlyStudentReadingDto = null;
        studentReadingBookDto = null;
    }

    @Test
    @WithMockUser(username = "testUser", authorities = {"STUDENT"})
    void createStudent() throws Exception{
        when(service.createStudent(any(StudentDto.class))).thenReturn(studentDto);

        mockMvc.perform(post("/student/createStudent")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(studentDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rauf Bayramov"));
        verify(service, times(1)).createStudent(any(StudentDto.class));
    }

    @Test
    @WithMockUser(username = "testUser", authorities = {"STUDENT"})
    void currentlyReadingBooks() throws Exception{
        long bookId = 5L;
        when(service.findById(bookId)).thenReturn(List.of(currentlyStudentReadingDto));

        mockMvc.perform(get("/student/currentlyReadingBooks/{id}", bookId)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currentlyReadingBooks[0].name").value("Ferrarisini satan rahib"));

        verify(service,times(1)).findById(bookId);
    }

    @Test
    void findAll() throws Exception{
        when(service.findAll()).thenReturn(List.of(studentDto));

        mockMvc.perform(get("/student/find-all")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rauf Bayramov"));

        verify(service, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "testUser", authorities = {"STUDENT"})
    void getStudentsWithSpecificBook() throws Exception {
        String bookName = "bookName";
        when(service.findByCurrentlyReadingBooks(bookName)).thenReturn(List.of(studentReadingBookDto));

        mockMvc.perform(get("/student/{bookName}", bookName)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rauf Bayramov"));

        verify(service,times(1)).findByCurrentlyReadingBooks(bookName);
    }
}