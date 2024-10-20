package com.example.bookstorefull.mapper;

import com.example.bookstorefull.dto.AuthorDto;
import com.example.bookstorefull.dto.BookDto;
import com.example.bookstorefull.dto.SubscribedAuthorDto;
import com.example.bookstorefull.model.Author;
import com.example.bookstorefull.model.Book;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDto authorToAuthorDto(Author author);
    @InheritInverseConfiguration
    Author authorDtoToAuthor(AuthorDto authorDto);

    SubscribedAuthorDto authorToSubscribedAuthorDto(Author author);
    @InheritInverseConfiguration
    Author subscribedAuthorDtoToAuthor(SubscribedAuthorDto subscribedAuthorDto);
}
