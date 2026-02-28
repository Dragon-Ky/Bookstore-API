package com.example.bookstore.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowResponse {
    Long id;
    String userEmail;
    String bookTitle;
    LocalDateTime borrowDate;
    LocalDateTime dueDate;
    String status;
}
