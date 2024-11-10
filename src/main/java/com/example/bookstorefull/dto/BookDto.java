package com.example.bookstorefull.dto;

import com.example.bookstorefull.model.Author;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDto  implements Serializable {
    String name;
}
