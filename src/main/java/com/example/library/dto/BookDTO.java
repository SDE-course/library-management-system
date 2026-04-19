package com.example.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class BookDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "ISBN is required")
        private String isbn;

        private String genre;
        private String publishedYear;

        @NotNull(message = "Author ID is required")
        private Long authorId; // We send the ID of the author to link them
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String isbn;
        private String genre;
        private String publishedYear;
        private boolean available;
        private String authorName; // Flattened name (e.g., "Jane Austen")
    }
}