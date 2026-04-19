package com.example.library.mapper;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    // Entity -> Response DTO
    public BookDTO.Response toResponse(Book book) {
        if (book == null) return null;

        return BookDTO.Response.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .genre(book.getGenre())
                .publishedYear(book.getPublishedYear())
                .available(book.isAvailable())
                .authorName(book.getAuthor() != null ? 
                    book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName() : null)
                .build();
    }

    // Request DTO -> Entity
    public Book toEntity(BookDTO.Request request) {
        if (request == null) return null;

        return Book.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .genre(request.getGenre())
                .publishedYear(request.getPublishedYear())
                // Note: The Service layer will handle setting the actual Author object
                .build();
    }

    // Update existing Entity from Request
    public void updateEntityFromRequest(BookDTO.Request request, Book book) {
        if (request == null) return;

        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setGenre(request.getGenre());
        book.setPublishedYear(request.getPublishedYear());
        // Service layer will handle the author_id change if needed
    }
}