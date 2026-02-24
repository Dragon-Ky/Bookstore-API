package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.Request.BookRequest;
import com.example.bookstore.DTO.Response.BookResponse;
import com.example.bookstore.Entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)// Để Spring Boot quản lý Mapper này như một Bean
public interface BookMapper {
    //chuyển từ Entity sang DTO (đưa cho người dùng)
    BookResponse toBookResponse(Book book);
    //Chuyển từ DTO sang Entity (đưa cho người xem)
    Book toBook(BookRequest request);
}
