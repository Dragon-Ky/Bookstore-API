package com.example.bookstore.Controller;

import com.example.bookstore.DTO.ApiResponse;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final GeminiService geminiService;

    @PostMapping("/search")
    public ApiResponse<List<Book>> chatToSearch(@RequestBody Map<String, String> request) {
        try {
            // 1. In ra chính xác những gì React đang gửi lên
            System.out.println("========== BẮT ĐẦU DEBUG ==========");
            System.out.println("1. Dữ liệu từ React gửi lên: " + request);

            String userMessage = request.get("message");
            System.out.println("2. Biến userMessage bóc tách được: " + userMessage);

            // 3. Gọi Service
            System.out.println("3. Đang gọi Google Gemini API...");
            List<Book> books = geminiService.processAiChat(userMessage);

            System.out.println("4. Tìm thấy số sách: " + (books != null ? books.size() : "null"));
            System.out.println("========== KẾT THÚC THÀNH CÔNG ==========");

            return ApiResponse.<List<Book>>builder()
                    .result(books)
                    .build();

        } catch (Exception e) {
            // 4. Nếu sập ở bất kỳ đâu, in toàn bộ chi tiết lỗi màu đỏ ra Console
            System.err.println("========== PHÁT HIỆN LỖI SẬP BACKEND ==========");
            System.err.println("Nguyên nhân chính: " + e.getMessage());
            e.printStackTrace();
            System.err.println("===============================================");
            throw e;
        }
    }
}