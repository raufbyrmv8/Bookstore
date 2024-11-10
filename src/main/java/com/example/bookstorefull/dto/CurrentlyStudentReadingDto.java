package com.example.bookstorefull.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrentlyStudentReadingDto implements Serializable {
    List<BookDto>currentlyReadingBooks;
}
