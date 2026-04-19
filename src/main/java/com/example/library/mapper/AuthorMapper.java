package com.example.library.mapper;

import com.example.library.dto.AuthorDTO;
import com.example.library.dto.BookDTO;      // Add this import
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    // Request DTO → Entity
    Author toEntity(AuthorDTO.Request request);

    // Entity → Response DTO
    AuthorDTO.Response toResponse(Author author);

    @Mapping(target = "author.id", source = "author.id")
    @Mapping(target = "author.firstName", source = "author.firstName")
    @Mapping(target = "author.lastName", source = "author.lastName")
    BookDTO.Response toBookResponse(Book book);

    List<BookDTO.Response> toBookResponseList(List<Book> books);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(AuthorDTO.Request request, @MappingTarget Author author);
}