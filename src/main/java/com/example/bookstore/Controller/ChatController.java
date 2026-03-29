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
        String userMessage = request.get("message");
        List<Book> books = geminiService.processAiChat(userMessage);

        // Thêm <List<Book>> ngay sau chữ builder
        return ApiResponse.<List<Book>>builder()
                .code(1000)
                .message("gọi API chatbox thành công")
                .result(books)
                .build();
    }
}