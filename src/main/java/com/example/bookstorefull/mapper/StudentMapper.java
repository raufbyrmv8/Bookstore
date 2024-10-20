package com.example.bookstorefull.mapper;

import com.example.bookstorefull.dto.StudentDto;
import com.example.bookstorefull.model.Student;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    StudentDto studentToStudentDto(Student student);
    @InheritInverseConfiguration
    Student studentDtotoStudent(StudentDto studentDto);
}
