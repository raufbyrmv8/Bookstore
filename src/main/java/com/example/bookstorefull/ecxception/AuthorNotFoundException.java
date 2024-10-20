package com.example.bookstorefull.ecxception;

public class AuthorNotFoundException extends NotFoundException{
    public AuthorNotFoundException(ErrorCode code, String ... args){
        super(code, args);
    }
}
