package com.example.library.controller;

import com.example.library.dto.AuthorDTO;
import com.example.library.dto.BookDTO;
import com.example.library.service.AuthorService;
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
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    // GET /api/authors?page=0&size=10&sort=lastName,asc
    @GetMapping
    public ResponseEntity<Page<AuthorDTO.Response>> getAllAuthors(
            @PageableDefault(size = 10, sort = "lastName") Pageable pageable) {
        return ResponseEntity.ok(authorService.getAllAuthors(pageable));
    }

    // GET /api/authors/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO.Response> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    // POST /api/authors
    @PostMapping
    public ResponseEntity<AuthorDTO.Response> createAuthor(
            @Valid @RequestBody AuthorDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorService.createAuthor(request));
    }

    // PUT /api/authors/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO.Response> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody AuthorDTO.Request request) {
        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

    // DELETE /api/authors/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/authors/{id}/books
    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDTO.Response>> getBooksByAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getBooksByAuthor(id));
    }
}
