package com.example.library.service;

import com.example.library.dto.*;
import com.example.library.entity.Member;
import com.example.library.exception.*;
import com.example.library.mapper.MemberMapper;
import com.example.library.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository repo;
    private final MemberMapper mapper;

    public MemberService(MemberRepository repo, MemberMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public MemberDTO.Response create(MemberDTO.Request dto) {
        if (repo.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Member member = mapper.toEntity(dto);
        return mapper.toResponse(repo.save(member));
    }

    public Page<MemberDTO.Response> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(mapper::toResponse);
    }

    public MemberDTO.Response getById(Long id) {
        Member m = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        return mapper.toResponse(m);
    }

    public MemberDTO.Response update(Long id, MemberDTO.Request dto) {
        Member m = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        if (!m.getEmail().equalsIgnoreCase(dto.getEmail()) && repo.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        m.setFirstName(dto.getFirstName());
        m.setLastName(dto.getLastName());
        m.setEmail(dto.getEmail());
        m.setPhoneNumber(dto.getPhoneNumber());

        return mapper.toResponse(repo.save(m));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Member not found");
        }
        repo.deleteById(id);
    }

    public List<MemberDTO.Response> search(String name) {
        return repo
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}