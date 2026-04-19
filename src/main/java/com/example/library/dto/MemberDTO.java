package com.example.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

// ─── Request DTO ───────────────────────────────────────────────
public class MemberDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message = "First name is required")
        private String firstName;

        @NotBlank(message = "Last name is required")
        private String lastName;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        private String phoneNumber;
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
        private String email;
        private String phoneNumber;
        private LocalDate membershipDate;
    }
}
