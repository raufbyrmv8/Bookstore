package com.example.bookstorefull.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    int age;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Book>books;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
            @JoinTable(name = "subscribedStudent_subscribedAuthor",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    List<Student>subscribedStudents;
}
