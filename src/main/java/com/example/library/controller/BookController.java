package com.example.library.controller;

import com.example.library.dto.BookDTO; // Capital B
import com.example.library.service.BookService; // Capital B
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService; // Use proper class name

    @GetMapping
    public ResponseEntity<Page<BookDTO.Response>> getAllBooks(
            @PageableDefault(size = 10, sort = "title") Pageable pageable) { // Sorted by title
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookDTO.Response> createBook(
            @Valid @RequestBody BookDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.Response> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDTO.Request request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // This fulfills your requirement: Search books by title, genre, and year
    @GetMapping("/search")
    public ResponseEntity<List<BookDTO.Response>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String publishedYear) {
        return ResponseEntity.ok(bookService.searchBooks(title, genre, publishedYear));
    }
}