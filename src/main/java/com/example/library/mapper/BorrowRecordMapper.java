package com.example.library.mapper;

import com.example.library.dto.BorrowRecordDTO;
import com.example.library.entity.BorrowRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BorrowRecordMapper {

    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "memberName", expression = "java(borrowRecord.getMember().getFirstName() + \" \" + borrowRecord.getMember().getLastName())")
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    BorrowRecordDTO.Response toResponse(BorrowRecord borrowRecord);

    List<BorrowRecordDTO.Response> toResponseList(List<BorrowRecord> borrowRecords);
}
