package com.example.library.mapper;

import com.example.library.dto.MemberDTO;
import com.example.library.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    Member toEntity(MemberDTO.Request dto);

    MemberDTO.Response toResponse(Member member);
}