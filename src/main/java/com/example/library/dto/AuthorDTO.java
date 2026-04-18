package com.example.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

// ─── Request DTO ───────────────────────────────────────────────
public class AuthorDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message = "First name is required")
        private String firstName;

        @NotBlank(message = "Last name is required")
        private String lastName;

        private String nationality;

        private LocalDate birthDate;
    }

    // ─── Response DTO ──────────────────────────────────────────
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private Long id;
        private String firstName;
        private String lastName;
        private String nationality;
        private LocalDate birthDate;
    }
}
