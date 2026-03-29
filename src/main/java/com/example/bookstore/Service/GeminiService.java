package com.example.bookstore.Service;

import com.example.bookstore.DTO.Response.AiSearchCriteria;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    // Model router tự động chọn các model free tốt nhất (Gemma 3, Llama 3.3, Mistral...)
    private static final String AUTO_FREE_MODEL = "openrouter/free";

    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<Book> processAiChat(String userInput) {
        // System Prompt: Quy định nghiêm ngặt định dạng JSON trả về

    String systemPrompt = """
        Bạn là máy trích xuất dữ liệu JSON cho nhà sách. Nhiệm vụ: Chuyển câu nói của khách thành JSON chuẩn.
        
        CẤU TRÚC JSON: {"title": "...", "author": "...", "category": "...", "keyword": "...", "orderParam": "..."}
    
        QUY TẮC PHÂN LOẠI (BẮT BUỘC):
        
        1. TRƯỜNG HỢP TÌM ĐÍCH DANH (Ví dụ: 'Doraemon', 'sách Conan', 'tác giả Kỳ'):
           - Chỉ đưa tên riêng vào 'title' hoặc 'author'.
           - CẤM tự ý điền 'category' hoặc 'keyword' nếu khách không nhắc đến từ khóa thể loại.
           - Loại bỏ từ hành động: 'tìm', 'mua', 'xem', 'sách của', 'truyện của'.
           - Ví dụ: 'tìm sách Doraemon' -> {"title": "Doraemon", "author": null, "category": null, "keyword": null}
    
        2. TRƯỜNG HỢP NHỜ GỢI Ý/TƯ VẤN (Ví dụ: 'gợi ý truyện hay', 'sách nào cho thiếu nhi'):
           - Đưa yêu cầu vào 'category' hoặc 'keyword'.
           - 'title' phải để null để hệ thống tìm rộng ra.
           - 'đọc gì đây', 'gợi ý đi' -> set orderParam = 'RANDOM_SUGGESTION'.
    
        3. QUY TẮC CỤ THỂ:
           - 'truyện [Tên]' -> title: '[Tên]', category: 'Truyện'.
           - Nếu khách chỉ gõ 1 từ (Ví dụ: 'Doraemon') -> mặc định đó là 'title'. Không được suy luận sang 'Anime'.
    
        PHẢI TRẢ VỀ DUY NHẤT JSON. KHÔNG GIẢI THÍCH.
        """;
        String fullPrompt = systemPrompt + "\nKhách hàng nói: " + userInput;
        String aiContent = null;

        try {
            // Bước 1: Gọi AI qua router tự động
            aiContent = callAI(AUTO_FREE_MODEL, fullPrompt);
        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối OpenRouter: " + e.getMessage());
        }

        // Bước 2: Xử lý kết quả trả về
        if (aiContent != null) {
            try {
                // Làm sạch chuỗi: Chỉ lấy nội dung từ dấu { đến dấu } cuối cùng để tránh văn bản thừa của AI
                int firstBrace = aiContent.indexOf("{");
                int lastBrace = aiContent.lastIndexOf("}");
                if (firstBrace != -1 && lastBrace != -1) {
                    aiContent = aiContent.substring(firstBrace, lastBrace + 1);
                }

                AiSearchCriteria criteria = objectMapper.readValue(aiContent, AiSearchCriteria.class);

                // Xử lý logic Random sách
                if ("RANDOM_SUGGESTION".equals(criteria.getOrderParam())) {
                    return bookRepository.getRandomBooks();
                }

                // Xử lý logic Sort
                Sort sort = Sort.unsorted();
                if ("LATEST".equals(criteria.getOrderParam())) {
                    sort = Sort.by(Sort.Direction.DESC, "id");
                }

                // Truy vấn DB dựa trên phân tích của AI
                return bookRepository.searchDynamicByAi(
                        criteria.getTitle(), criteria.getAuthor(),
                        criteria.getCategories(), criteria.getKeyword(), sort
                );

            } catch (Exception e) {
                System.err.println("❌ Lỗi parse JSON hoặc DB: " + e.getMessage());
            }
        }

        // Bước 3: FALLBACK CUỐI CÙNG (Nếu AI chết hoặc JSON lỗi)
        // Tìm kiếm trực tiếp bằng câu chat của khách vào database
        System.out.println("⚠️ Sử dụng tìm kiếm Keyword dự phòng");
        return bookRepository.searchDynamicByAi(null, null, null, userInput, Sort.unsorted());
    }

    private String callAI(String model, String prompt) {
        String url = "https://openrouter.ai/api/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("HTTP-Referer", "http://localhost:8080"); // Bắt buộc cho OpenRouter
        headers.set("X-Title", "BookStore AI");

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.1 // Giảm sáng tạo để JSON ra chuẩn hơn
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);

        JsonNode root = response.getBody();
        if (root == null || !root.has("choices")) {
            throw new RuntimeException("AI response invalid");
        }

        return root.get("choices").get(0).get("message").get("content").asText();
    }
}