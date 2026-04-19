package com.example.library.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

public class BorrowRecordDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotNull(message = "Member ID is required")
        private Long memberId;

        @NotNull(message = "Book ID is required")
        private Long bookId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long memberId;
        private String memberName;
        private Long bookId;
        private String bookTitle;
        private LocalDate borrowDate;
        private LocalDate returnDate;
        private boolean returned;
    }
}
