package com.example.library.controller;

import com.example.library.dto.BorrowRecordDTO;
import com.example.library.service.BorrowRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;

    @PostMapping
    public ResponseEntity<BorrowRecordDTO.Response> borrowBook(
            @Valid @RequestBody BorrowRecordDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowRecordService.borrowBook(request));
    }

    @PutMapping("/{borrowRecordId}/return")
    public ResponseEntity<BorrowRecordDTO.Response> returnBook(@PathVariable Long borrowRecordId) {
        return ResponseEntity.ok(borrowRecordService.returnBook(borrowRecordId));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BorrowRecordDTO.Response>> getBorrowsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(borrowRecordService.getBorrowsByMember(memberId));
    }
}
