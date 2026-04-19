package com.example.library.service;

import com.example.library.dto.AuthorDTO;
import com.example.library.dto.BookDTO;
import com.example.library.entity.Author;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.mapper.AuthorMapper;
import com.example.library.mapper.BookMapper;
import com.example.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    // GET /api/authors — paginated
    public Page<AuthorDTO.Response> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable)
                .map(authorMapper::toResponse);
    }

    // GET /api/authors/{id}
    public AuthorDTO.Response getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return authorMapper.toResponse(author);
    }

    // POST /api/authors
    @Transactional
    public AuthorDTO.Response createAuthor(AuthorDTO.Request request) {
        Author author = authorMapper.toEntity(request);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    // PUT /api/authors/{id}
    @Transactional
    public AuthorDTO.Response updateAuthor(Long id, AuthorDTO.Request request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        authorMapper.updateEntityFromRequest(request, author);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    // DELETE /api/authors/{id}
    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    // GET /api/authors/{id}/books
    // Uses JOIN FETCH to avoid N+1 problem
    public List<BookDTO.Response> getBooksByAuthor(Long id) {
        Author author = authorRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return author.getBooks().stream()
            .map(bookMapper::toResponse)
            .collect(Collectors.toList());
    }
}
