package com.example.bookstore.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookRequest {
    @NotBlank(message="NOT_BLANK_IMG")
    String image;

    @NotBlank(message = "NOT_BLANK_TITLE")
    @Size(min = 3, max = 100, message = "INVALID_TITLE")
    String title;

    @NotBlank(message = "NOT_BLANK_AUTHOR")
    String author;

    @NotNull(message = "NOT_NULL_CATEGORY")
    String category;

    @NotBlank(message = "NOT_BLANK_DESCRIPTION")
    String description;

    @Min(value = 1, message = "QUANTITY_INVALID")
    int totalQuantity;
}
