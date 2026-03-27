package com.example.bookstore.Service;

import com.example.bookstore.DTO.Response.AiSearchCriteria;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {
    @Value("${gemini.api.key}") // Lấy key từ application.properties
    private String apiKey;

    private final RestTemplate restTemplate; // Công cụ gọi HTTP
    private final BookRepository bookRepository; // Tương tác DB
    private final ObjectMapper objectMapper; // Công cụ chuyển JSON thành Java Object

    public List<Book> processAiChat(String userInput) {
        // 1. Tạo câu lệnh điều khiển (Prompt) ép AI trả về JSON
        String systemPrompt = "Bạn là trợ lý tìm sách. Phân tích câu của người dùng và trả về DUY NHẤT 1 chuỗi JSON.\n" +
                "Cấu trúc: {\"title\": \"tên sách\", \"author\": \"tác giả\", \"orderParam\": null}.\n" +
                "Nếu không rõ thông tin nào, để null. KHÔNG trả về markdown hay văn bản nào khác.";

        String fullPrompt = systemPrompt + "\nCâu hỏi: " + userInput;

        // 2. Cấu hình URL và Header cho API của Google
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. Đóng gói dữ liệu thành chuẩn Body mà Google yêu cầu
        Map<String, Object> body = Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", fullPrompt)))));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // 4. Gửi Request và nhận phản hồi
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);

            // 5. Bóc tách JSON lồng nhau để lấy đoạn text AI trả lời
            String aiContent = response.getBody().get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();

            // 6. Xóa các ký tự thừa (```json) nếu AI lỡ sinh ra
            aiContent = aiContent.replaceAll("```json", "").replaceAll("```", "").trim();

            // 7. Chuyển đổi chuỗi JSON thành Object Java (AiSearchCriteria)
            AiSearchCriteria criteria = objectMapper.readValue(aiContent, AiSearchCriteria.class);

            // 8. Chạy SQL query vào Database dựa trên dữ liệu AI cung cấp
            return bookRepository.searchDynamicByAi(criteria.getTitle(), criteria.getAuthor());

        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý AI: " + e.getMessage());
        }
    }
}