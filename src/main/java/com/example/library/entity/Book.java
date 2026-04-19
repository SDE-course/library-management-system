package com.example.library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id   //Marks the id field as the Primary Key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB handle the numbering automatically.
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)  //Ensures no two books have the same ISBN.
    private String isbn;

    @Column
    private String genre;

    @Column
    private String publishedYear;

    // This links directly to your Author entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false) // Foreign key to the Author table
    private Author author;
}