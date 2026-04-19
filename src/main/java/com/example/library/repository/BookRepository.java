package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Search books by title, genre, OR year.
     * Spring Data JPA will automatically generate the SQL for this based on the method name!
     */
    List<Book> findByTitleContainingOrGenreOrPublishedYear(String title, String genre, String publishedYear);

    // Optional: If you need to check if an ISBN exists before saving
    boolean existsByIsbn(String isbn);
}