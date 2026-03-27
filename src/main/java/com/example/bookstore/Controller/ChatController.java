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
        // 1. Lấy biến "message" từ Frontend gửi lên
        String userMessage = request.get("message");

        // 2. Gọi Service xử lý
        List<Book> books = geminiService.processAiChat(userMessage);

        // 3. Trả danh sách Sách về dưới dạng chuẩn ApiResponse
        return ApiResponse.<List<Book>>builder()
                .result(books)
                .build();
    }
}