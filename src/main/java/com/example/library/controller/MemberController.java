package com.example.library.controller;
import jakarta.validation.Valid;

import com.example.library.dto.*;
import lombok.RequiredArgsConstructor;
import com.example.library.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;



    @PostMapping
    public ResponseEntity<MemberDTO.Response> create(@Valid @RequestBody MemberDTO.Request dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<MemberDTO.Response>> getAll(
            @PageableDefault(size = 10, sort = "lastName") Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO.Response> update(
            @PathVariable Long id,
            @Valid @RequestBody MemberDTO.Request dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<MemberDTO.Response>> search(@RequestParam String name) {
        return ResponseEntity.ok(service.search(name));
    }
}