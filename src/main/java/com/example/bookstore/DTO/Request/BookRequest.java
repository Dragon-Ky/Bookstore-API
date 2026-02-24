package com.example.bookstore.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookRequest {
    @NotBlank(message = "NOT_BLANK_TITLE")
    @Size(min = 2, max = 100, message = "INVALID_TITLE")
    private String title;

    @NotBlank(message = "NOT_BLANK_AUTHOR")
    private String author;

    @NotBlank(message = "NOT_NULL_CATEGORY")
    private String category;

    @Min(value = 1, message = "QUANTITY_INVALID")
    private int totalQuantity;
}
