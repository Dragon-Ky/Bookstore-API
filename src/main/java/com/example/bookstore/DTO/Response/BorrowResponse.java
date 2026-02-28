package com.example.bookstore.DTO.Response;

import com.example.bookstore.Entity.ENUM.BorrowStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // trả về định dạng đẹp hơn
    LocalDateTime borrowDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dueDate;

    BorrowStatus status;

}
