package com.example.library.mapper;

import com.example.library.dto.MemberRequestDTO;
import com.example.library.dto.MemberResponseDTO;
import com.example.library.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    Member toEntity(MemberRequestDTO dto);

    MemberResponseDTO toResponse(Member member);
}