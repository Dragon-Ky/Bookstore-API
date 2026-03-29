package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.Request.BookRequest;
import com.example.bookstore.DTO.Response.BookResponse;
import com.example.bookstore.Entity.Book;
import org.mapstruct.*;
import org.mapstruct.Named;

// Quan trọng: Thêm "uses" để MapStruct gọi được class Helper
@Mapper(componentModel = "spring", uses = BookMapperHelper.class)
public interface BookMapper {

    @Mapping(target = "categories", source = "categories", qualifiedByName = "stringToSet")
    Book toBook(BookRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availableQuantity", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "categories", source = "categories", qualifiedByName = "stringToSet")
    void updateBook(@MappingTarget Book book, BookRequest request);

    // nếu response là Set thì KHÔNG cần mapping
    BookResponse toBookResponse(Book book);
}