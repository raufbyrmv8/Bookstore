package com.example.bookstorefull.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseStudentReadingBookDto implements Serializable {
    String name;
    int age;
}
