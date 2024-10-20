package com.example.bookstorefull.dto;

import com.example.bookstorefull.model.Student;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscribedAuthorDto {
    String name;
    int age;
    List<SubscribedStudentDto> subscribedStudents;
}
