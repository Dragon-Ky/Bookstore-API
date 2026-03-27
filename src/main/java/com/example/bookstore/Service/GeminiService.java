package com.example.bookstore.Service;

import com.example.bookstore.DTO.Response.AiSearchCriteria;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<Book> processAiChat(String userInput) {

        // 1. DẠY AI NHẬN DIỆN THỂ LOẠI VÀ NỘI DUNG (CẬP NHẬT PROMPT MỚI)
        String systemPrompt = "Bạn là thủ thư nhà sách xuất sắc. Phân tích yêu cầu của khách và trả về chuẩn JSON: {\"title\": \"tên\", \"author\": \"tác giả\", \"category\": \"thể loại\", \"keyword\": \"từ khóa\", \"orderParam\": \"chuỗi\"}.\n" +
                "QUY TẮC BÓC TÁCH:\n" +
                "1. Nếu khách nhắc đến thể loại (ví dụ: lãng mạn, trinh thám, thiếu nhi, IT, kinh doanh...), gán vào 'category'.\n" +
                "2. Nếu khách mô tả cốt truyện hoặc nhu cầu (ví dụ: 'sách về du hành thời gian', 'chữa lành tâm hồn', 'học lập trình java'), trích xuất từ khóa chính gán vào 'keyword'.\n" +
                "3. Nếu khách nhờ gợi ý ngẫu nhiên ('đọc gì đây', 'sách hay'), gán orderParam = 'RANDOM_SUGGESTION'.\n" +
                "4. Cái nào không có thông tin thì để null.";

        String fullPrompt = systemPrompt + "\nKhách hàng nói: " + userInput;

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", fullPrompt)))),
                "generationConfig", Map.of("responseMimeType", "application/json")
        );
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
            JsonNode candidate = response.getBody().get("candidates").get(0);
            String aiContent = candidate.get("content").get("parts").get(0).get("text").asText();

            // ObjectMapper sẽ tự động map thêm biến category và keyword nhờ cấu hình mới ở DTO
            AiSearchCriteria criteria = objectMapper.readValue(aiContent, AiSearchCriteria.class);

            // Xử lý gợi ý ngẫu nhiên
            if ("RANDOM_SUGGESTION".equals(criteria.getOrderParam())) {
                return bookRepository.getRandomBooks();
            }

            Sort sort = Sort.unsorted();
            if ("LATEST".equals(criteria.getOrderParam())) {
                sort = Sort.by(Sort.Direction.DESC, "id");
            }

            // 2. GỌI TRUY VẤN VỚI ĐẦY ĐỦ 5 THAM SỐ
            return bookRepository.searchDynamicByAi(
                    criteria.getTitle(),
                    criteria.getAuthor(),
                    criteria.getCategory(),
                    criteria.getKeyword(),
                    sort
            );

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Lỗi từ máy chủ Google: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi hệ thống khi parse JSON hoặc kết nối DB: " + e.getMessage(), e);
        }
    }
}