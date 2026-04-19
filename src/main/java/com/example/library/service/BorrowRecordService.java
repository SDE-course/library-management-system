package com.example.library.service;

import com.example.library.dto.BorrowRecordDTO;
import com.example.library.entity.Book;
import com.example.library.entity.BorrowRecord;
import com.example.library.entity.Member;
import com.example.library.exception.DuplicateResourceException;
import com.example.library.exception.OperationConflictException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRecordRepository;
import com.example.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    @Transactional
    public BorrowRecordDTO.Response borrowBook(BorrowRecordDTO.Request request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + request.getMemberId()));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + request.getBookId()));

        if (!book.isAvailable() || borrowRecordRepository.existsByBookIdAndReturnedFalse(book.getId())) {
            throw new OperationConflictException("Book is not available for borrowing");
        }

        if (borrowRecordRepository.existsByMemberIdAndBookIdAndReturnedFalse(member.getId(), book.getId())) {
            throw new DuplicateResourceException("This member already has an active borrow for the same book");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        BorrowRecord record = BorrowRecord.builder()
                .member(member)
                .book(book)
                .borrowDate(LocalDate.now())
                .returned(false)
                .build();

        BorrowRecord saved = borrowRecordRepository.save(record);
        return toResponse(saved);
    }

    @Transactional
    public BorrowRecordDTO.Response returnBook(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findByIdWithDetails(borrowRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with id: " + borrowRecordId));

        if (record.isReturned()) {
            throw new OperationConflictException("Book is already returned");
        }

        record.setReturned(true);
        record.setReturnDate(LocalDate.now());

        Book book = record.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        BorrowRecord saved = borrowRecordRepository.save(record);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BorrowRecordDTO.Response> getBorrowsByMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("Member not found with id: " + memberId);
        }

        return borrowRecordRepository.findByMemberId(memberId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private BorrowRecordDTO.Response toResponse(BorrowRecord borrowRecord) {
        String memberName = borrowRecord.getMember().getFirstName() + " " + borrowRecord.getMember().getLastName();
        return BorrowRecordDTO.Response.builder()
                .id(borrowRecord.getId())
                .memberId(borrowRecord.getMember().getId())
                .memberName(memberName)
                .bookId(borrowRecord.getBook().getId())
                .bookTitle(borrowRecord.getBook().getTitle())
                .borrowDate(borrowRecord.getBorrowDate())
                .returnDate(borrowRecord.getReturnDate())
                .returned(borrowRecord.isReturned())
                .build();
    }
}
