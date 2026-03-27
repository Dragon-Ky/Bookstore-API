package com.example.bookstore.DTO.Response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AiSearchCriteria {
    String title;
    String author;
    String orderParam;
}
