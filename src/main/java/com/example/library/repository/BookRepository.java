package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(attributePaths = {"author"})
    Page<Book> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"author"})
    Optional<Book> findById(Long id);

    /**
     * Search books by title, genre, OR year.
     * Spring Data JPA will automatically generate the SQL for this based on the method name!
     */
    @EntityGraph(attributePaths = {"author"})
    List<Book> findByTitleContainingOrGenreOrPublishedYear(String title, String genre, String publishedYear);

    // Optional: If you need to check if an ISBN exists before saving
    boolean existsByIsbn(String isbn);
}