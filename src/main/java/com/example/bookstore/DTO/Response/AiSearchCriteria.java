package com.example.bookstore.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiSearchCriteria {
    private String title;
    private String author;

    // THÊM 2 DÒNG NÀY
    private String category; // Để hứng thể loại (lãng mạn, trinh thám...)
    private String keyword;  // Để hứng từ khóa nội dung (vũ trụ, tình yêu, ma thuật...)

    private String orderParam;
}