package com.example.bookstore.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookResponse {
     Long id;
     String image;
     String title;
     String author;
     Set<String> categories;
     String description;

     int totalQuantity;
     int availableQuantity; // Chỉ trả về số lượng còn lại, không cần trả về total
}