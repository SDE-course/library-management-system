package com.example.library.repository;

import com.example.library.entity.BorrowRecord;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    boolean existsByBookIdAndReturnedFalse(Long bookId);

    boolean existsByMemberIdAndBookIdAndReturnedFalse(Long memberId, Long bookId);

    @EntityGraph(attributePaths = {"member", "book"})
    @Query("select br from BorrowRecord br where br.id = :id")
    Optional<BorrowRecord> findByIdWithDetails(@Param("id") Long id);

    @EntityGraph(attributePaths = {"member", "book"})
    List<BorrowRecord> findByMemberId(Long memberId);

    @EntityGraph(attributePaths = {"member", "book"})
    List<BorrowRecord> findByReturnDateIsNull();
}
