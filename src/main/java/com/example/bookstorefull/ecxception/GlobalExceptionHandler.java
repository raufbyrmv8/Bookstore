package com.example.bookstorefull.ecxception;

import com.example.bookstorefull.service.TranslateService;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final TranslateService translateService;
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorExceptionHandle(AuthorNotFoundException e, WebRequest request){
        String language = request.getHeader("Accept-Language");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse
                .builder()
                .status(HttpStatus.NOT_FOUND.value())
                .code(e.getCode())
                        .message(translateService.translate(e.getCode().getCode(), language, e.getArgs()))
                        .detail(translateService.translate(e.getCode().getCode().concat("_DETAIL"), language, e.getArgs()))
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build());
    }
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStudentExceptionHandle(StudentNotFoundException e, WebRequest request){
        String language = request.getHeader("Accept-Language");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse
                        .builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .code(e.getCode())
                        .message(translateService.translate(e.getCode().getCode(), language, e.getArgs()))
                        .detail(translateService.translate(e.getCode().getCode().concat("_DETAIL"), language, e.getArgs()))
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
//                        .requestLang(request.getHeader("Accept-Language"))
                        .build());
    }
}
