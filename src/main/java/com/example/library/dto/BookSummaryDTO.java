package com.example.library.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookSummaryDTO {
    private Long id;
    private String title;
    private String isbn;
    private String genre;
    private Integer publishedYear;
}
