package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.Response.BorrowResponse;
import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Entity.BorrowRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BorrowMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "BORROWING")
    BorrowRecord toBorrow(AppUser user, Book book);

    @Mapping(target = "user.name", source = "user.name")
    @Mapping(target = "user.email", source = "user.email")
    @Mapping(target = "bookTitle", source = "book.title")
    BorrowResponse toResponse(BorrowRecord record);
}
