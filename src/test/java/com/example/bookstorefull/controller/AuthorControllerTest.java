package com.example.bookstorefull.controller;

import com.example.bookstorefull.dto.AuthorDto;
import com.example.bookstorefull.dto.BookDto;
import com.example.bookstorefull.dto.SubscribedAuthorDto;
import com.example.bookstorefull.service.AuthorService;
import com.example.bookstorefull.service.BookPublishingService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureMockMvc
class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService service;
    @MockBean
    private BookPublishingService bookPublishingService;
    private BookDto bookDto;
    private AuthorDto authorDto;
    private SubscribedAuthorDto subscribedAuthorDto;

    @BeforeEach
    void setUp() {
        authorDto = AuthorDto
                .builder()
                .age(25)
                .name("Robin Sharma")
                .books(List.of(BookDto
                        .builder()
                        .name("Ferrarisini satan rahib")
                        .build()))
                .build();
        bookDto = BookDto
                .builder()
                .name("Ferrarisini satan rahib")
                .build();
       subscribedAuthorDto = SubscribedAuthorDto
               .builder()
               .age(25)
               .name("Rauf Bayramov")
               .build();
    }

    @AfterEach
    void tearDown() {
        authorDto = null;
        bookDto = null;
        subscribedAuthorDto = null;
    }

    @Test
    @WithMockUser(username = "testUser", authorities = {"AUTHOR"})
    void createBook() throws Exception {
        when(service.createBook(any(AuthorDto.class))).thenReturn(authorDto);

        mockMvc.perform(post("/author/createBook")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(authorDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Robin Sharma"));

        verify(service,times(1)).createBook(any(AuthorDto.class));
    }

    @Test
    @WithMockUser(username = "testUser", authorities = "AUTHOR")
    void findByBook() throws Exception {
        String name = "bookName";
        when(service.findByBook(name)).thenReturn(authorDto);

        mockMvc.perform(get("/author/findBook/{name}", name)
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(authorDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Robin Sharma"));

        verify(service,times(1)).findByBook(name);
    }

    @Test
    @WithMockUser(username = "testUser", authorities = "AUTHOR")
    void deleteByBook() throws Exception {
        String name = "bookName";

        mockMvc.perform(delete("/author/delete/{name}",name))
                .andDo(print())
                .andExpect(status().isOk());

        verify(service, times(1)).deleteByBookName(name);
    }

    @Test
    @WithMockUser(username = "testUser", authorities = "AUTHOR")
    void updateAuthor() throws Exception {
        long authorId = 1l;
        when(service.updateAuthor(eq(authorId),any(AuthorDto.class))).thenReturn(authorDto);
        mockMvc.perform(put("/author/updateAuthor/{id}", authorId)
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(authorDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Robin Sharma"));

        verify(service, times(1)).updateAuthor(eq(authorId), any(AuthorDto.class));
    }

    @Test
    @WithMockUser(username = "testUser", authorities = "AUTHOR")
    void findAll() throws Exception {
        when(service.findAll()).thenReturn(List.of(authorDto));

        mockMvc.perform(get("/author/find-all")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Robin Sharma"));

        verify(service, times(1)).findAll();
    }

    @Test
    void findAllByName() throws Exception {
        String name = "bookName";
        when(service.findAllByName(name)).thenReturn(List.of(authorDto));

        mockMvc.perform(get("/author/find-all/{name}",name)
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(authorDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Robin Sharma"));

        verify(service, times(1)).findAllByName(name);
    }

    @Test
    @WithMockUser(username = "testUser", authorities = {"AUTHOR","STUDENT"})
    void findByBookWithAuthorName() throws Exception {
        String authorName = "authorName";
        when(service.findByBookWithAuthorName(authorName)).thenReturn(List.of(bookDto));

        mockMvc.perform(get("/author/{authorName}", authorName)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ferrarisini satan rahib"));

        verify(service, times(1)).findByBookWithAuthorName(authorName);
    }

    @Test
    @WithMockUser(username = "testUser",authorities = "AUTHOR")
    void subscribedSave() throws Exception {
        when(service.subscribedSave(any(SubscribedAuthorDto.class))).thenReturn(subscribedAuthorDto);

        mockMvc.perform(post("/author/subscribedSave")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(subscribedAuthorDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rauf Bayramov"));

        verify(service,times(1)).subscribedSave(any(SubscribedAuthorDto.class));

    }

    @Test
    @WithMockUser(username = "testUser", authorities = "AUTHOR")
    void publishBook() throws Exception{
        long authorId = 1l;
        mockMvc.perform(post("/author"+ "/" + authorId + "/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookDto)))
                .andExpect(status().isOk());

        verify(bookPublishingService, times(1)).publishBook(authorId, bookDto);
    }
}