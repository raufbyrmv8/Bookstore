package com.example.bookstorefull.controller;

import com.example.bookstorefull.service.TranslateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final TranslateService translateService;
    @GetMapping("/hello")
    public String hello(@RequestHeader (name = "Accept-Language")String language,
                        @RequestParam String name){
        return translateService.translate("welcomeMessage", name, language);
    }
}
