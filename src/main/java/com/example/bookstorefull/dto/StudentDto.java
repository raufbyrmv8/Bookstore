package com.example.bookstorefull.dto;

import com.example.bookstorefull.model.Book;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentDto {
    String name;
    int age;
    List<BookDto> currentlyReadingBooks;
}
