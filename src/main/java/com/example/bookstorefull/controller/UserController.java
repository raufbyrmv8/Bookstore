package com.example.bookstorefull.controller;

import com.example.bookstorefull.dto.RequestRegisterDto;
import com.example.bookstorefull.dto.ResponseRegisterDto;
import com.example.bookstorefull.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseRegisterDto register(@RequestBody RequestRegisterDto requestRegisterDto){
        return userService.register(requestRegisterDto);
    }
}
