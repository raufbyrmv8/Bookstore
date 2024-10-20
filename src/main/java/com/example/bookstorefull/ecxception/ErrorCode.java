package com.example.bookstorefull.ecxception;

import lombok.Getter;

@Getter
public enum ErrorCode {
   STUDENT_NOT_FOUND_0001("STUDENT_NOT_FOUND_0001"),
   AUTHOR_NOT_FOUND_0001("AUTHOR_NOT_FOUND_0001");

    private final String code;

    ErrorCode(String code){
        this.code = code;
    }
}
