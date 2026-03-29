package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.Request.BookRequest;
import com.example.bookstore.DTO.Response.BookResponse;
import com.example.bookstore.Entity.Book;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {

    // 1. Ánh xạ từ Entity sang Response
    BookResponse toBookResponse(Book book);

    // 2. Ánh xạ từ Request sang Entity
    // Nếu trong BookRequest.java bạn để trường là String categories:
    @Mapping(target = "categories", source = "categories", qualifiedByName = "stringToSet")
    Book toBook(BookRequest request);

    // 3. Cập nhật Entity từ Request
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", source = "categories", qualifiedByName = "stringToSet")
    void updateBook(@MappingTarget Book book, BookRequest bookRequest);

    // CHÚ Ý: Phải có @Named và đặt TRONG interface như một default method
    @Named("stringToSet")
    default Set<String> stringToSet(String categoriesStr) {
        if (categoriesStr == null || categoriesStr.isBlank()) {
            return null;
        }
        return Arrays.stream(categoriesStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}