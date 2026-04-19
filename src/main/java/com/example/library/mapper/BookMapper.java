package com.example.library.mapper;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "author.id", source = "author.id")
    @Mapping(target = "author.firstName", source = "author.firstName")
    @Mapping(target = "author.lastName", source = "author.lastName")
    BookDTO.Response toResponse(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "borrowRecords", ignore = true)
    @Mapping(target = "available", ignore = true)
    Book toEntity(BookDTO.Request request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "borrowRecords", ignore = true)
    @Mapping(target = "available", ignore = true)
    void updateEntityFromRequest(BookDTO.Request request, @MappingTarget Book book);
}