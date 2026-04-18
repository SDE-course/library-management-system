package com.example.library.mapper;

import com.example.library.dto.AuthorDTO;
import com.example.library.dto.BookSummaryDTO;
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

    // Book Entity → BookSummaryDTO (for /api/authors/{id}/books)
    BookSummaryDTO toBookSummary(Book book);

    List<BookSummaryDTO> toBookSummaryList(List<Book> books);

    // Update existing entity from request (ignore null fields)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(AuthorDTO.Request request, @MappingTarget Author author);
}
