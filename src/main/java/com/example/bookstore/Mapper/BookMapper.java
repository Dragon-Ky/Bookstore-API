package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.Request.BookRequest;
import com.example.bookstore.DTO.Response.BookResponse;
import com.example.bookstore.Entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)// Để Spring Boot quản lý Mapper này như một Bean
public interface BookMapper {
    //chuyển từ Entity sang DTO (đưa cho người dùng)
    @Mapping(source = "totalQuantity", target = "totalQuantity")
    BookResponse toBookResponse(Book book);
    //Chuyển từ DTO sang Entity (đưa cho người xem)
    Book toBook(BookRequest request);

    @Mapping(target = "id" , ignore = true) // ko cập nhật id
    void updateBook(@MappingTarget Book book,BookRequest bookRequest);


}
