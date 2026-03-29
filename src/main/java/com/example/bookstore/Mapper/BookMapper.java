package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.Request.BookRequest;
import com.example.bookstore.DTO.Response.BookResponse;
import com.example.bookstore.Entity.Book;
import org.mapstruct.*;

// Bạn có thể xóa luôn `uses = BookMapperHelper.class` nếu không dùng hàm nào khác trong đó
@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toBook(BookRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availableQuantity", ignore = true)
    @Mapping(target = "deleted", ignore = true)

    void updateBook(@MappingTarget Book book, BookRequest request);

    BookResponse toBookResponse(Book book);
}