package com.example.bookstorefull.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrentlyStudentReadingDto {
    List<BookDto>currentlyReadingBooks;
}
