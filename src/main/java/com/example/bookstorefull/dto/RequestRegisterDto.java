package com.example.bookstorefull.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestRegisterDto {
    String name;
    String password;
    String repeatPassword;
    List<String>authorities;
}
