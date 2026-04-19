package com.example.library.service;

import com.example.library.dto.BookDTO;          
import com.example.library.entity.Author;
import com.example.library.entity.Book;          
import com.example.library.exception.DuplicateResourceException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.mapper.BookMapper;    
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository; 
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public Page<BookDTO.Response> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toResponse);
    }

    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return bookMapper.toResponse(book);
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Book with the same ISBN already exists");
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + request.getAuthorId()));

        Book book = bookMapper.toEntity(request);
        book.setAuthor(author);
        book.setAvailable(true);
        author.getBooks().add(book);
        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        Author author = authorRepository.findById(request.getAuthorId())
            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + request.getAuthorId()));

        if (book.getAuthor() != null && book.getAuthor().getId() != null
                && !book.getAuthor().getId().equals(author.getId())) {
            book.getAuthor().getBooks().remove(book);
        }

        bookMapper.updateEntityFromRequest(request, book);
        book.setAuthor(author);
        if (!author.getBooks().contains(book)) {
            author.getBooks().add(book);
        }
        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    // This search logic matches the requirement for title, genre, etc.
    public List<BookDTO.Response> searchBooks(String title, String genre, String year) {
        // You will need to define this search method in your BookRepository
        List<Book> books = bookRepository.findByTitleContainingOrGenreOrPublishedYear(title, genre, year);
        return books.stream().map(bookMapper::toResponse).toList();
    }
}