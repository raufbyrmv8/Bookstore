package com.example.bookstorefull.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TranslateService {
    private final MessageSource messageSource;
    public String translate(String code, String language, String ... args){
        return messageSource.getMessage(code, args, new Locale(language));
    }
}
