package com.example.bookstorefull.controller;

import com.example.bookstorefull.dto.RequestRegisterDto;
import com.example.bookstorefull.dto.ResponseRegisterDto;
import com.example.bookstorefull.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    private ResponseRegisterDto responseRegisterDto;
    private RequestRegisterDto requestRegisterDto;

    @BeforeEach
    void setUp() {
        responseRegisterDto = ResponseRegisterDto
                .builder()
                .jwt("secretKey")
                .build();
        requestRegisterDto = RequestRegisterDto
                .builder()
                .name("Rauf Bayramov")
                .password("123456")
                .build();
    }

    @AfterEach
    void tearDown() {
        responseRegisterDto = null;
        requestRegisterDto = null;
    }

    @Test
    void register() throws Exception {
        when(userService.register(any(RequestRegisterDto.class))).thenReturn(responseRegisterDto);

        mockMvc.perform(post("/user/register")
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(requestRegisterDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("secretKey"));

        verify(userService, times(1)).register(any(RequestRegisterDto.class));
    }
}