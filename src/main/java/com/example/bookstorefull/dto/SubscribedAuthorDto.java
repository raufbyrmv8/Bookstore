package com.example.bookstorefull.dto;

import com.example.bookstorefull.model.Student;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscribedAuthorDto {
    String name;
    int age;
    List<SubscribedStudentDto> subscribedStudents;
}
