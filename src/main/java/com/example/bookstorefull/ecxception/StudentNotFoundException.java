package com.example.bookstorefull.ecxception;

public class StudentNotFoundException extends NotFoundException{
    public StudentNotFoundException(ErrorCode code, String ... args) {
        super(code, args);
    }
}
