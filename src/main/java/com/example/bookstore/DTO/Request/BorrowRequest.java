package com.example.bookstore.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowRequest {
    @NotNull(message = "BOOK_ID_REQUIRED")
    Long bookId;
}
