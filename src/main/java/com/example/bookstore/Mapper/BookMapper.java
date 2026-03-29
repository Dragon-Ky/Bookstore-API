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

    // Chuyển từ Entity sang Response (để hiển thị)
    BookResponse toBookResponse(Book book);

    // Chuyển từ Request sang Entity (để lưu vào DB)
    @Mapping(target = "categories", source = "categories", qualifiedByName = "stringToSet")
    Book toBook(BookRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", source = "categories", qualifiedByName = "stringToSet")
    void updateBook(@MappingTarget Book book, BookRequest bookRequest);

    // Logic tách chuỗi: "ANIME, NGON TINH" -> ["ANIME", "NGON TINH"]
    @Named("stringToSet")
    default Set<String> stringToSet(String category) {
        if (category == null || category.isBlank()) {
            return null;
        }
        return Arrays.stream(category.split(",")) // Tách theo dấu phẩy
                .map(String::trim)          // Xóa khoảng trắng thừa
                .filter(s -> !s.isEmpty())  // Loại bỏ các phần tử rỗng
                .collect(Collectors.toSet());
    }
}