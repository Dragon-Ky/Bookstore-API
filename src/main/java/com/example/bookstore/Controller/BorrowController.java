package com.example.bookstore.Controller;

import com.example.bookstore.DTO.ApiResponse;
import com.example.bookstore.DTO.Request.BorrowRequest;
import com.example.bookstore.DTO.Response.BorrowResponse;
import com.example.bookstore.Service.BorrowService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BorrowController {
    BorrowService borrowService;

    @PostMapping
    public ApiResponse<BorrowResponse> borrowBook(@RequestBody @Valid BorrowRequest request){
        // 1. Dữ liệu đã được chuyển từ JSON sang Object thành công (@RequestBody)
        // 2. Dữ liệu chắc chắn có title, image không trống, totalQuantity >= 1 (@Valid)
        return ApiResponse.<BorrowResponse> builder()
                .code(1000)
                .message("Mượn sách thành công!")
                .result(borrowService.borrowBook(request))
                .build();
    }

    @PostMapping("/{recordId}/return")
    public ApiResponse<BorrowResponse> returnBook(@PathVariable Long recordId){
        return ApiResponse.<BorrowResponse>builder()
                .code(1000)
                .message("Trả sách thành công!")
                .result(borrowService.returnBook(recordId))
                .build();
    }

    @GetMapping("/my-history")
    public ApiResponse<List<BorrowResponse>> getMyHistory(){
        return ApiResponse.<List<BorrowResponse>>builder()
                .code(1000)
                .message("Xem lịch sử mượn sách thành công")
                .result(borrowService.getMyHistory())
                .build();
    }
    @GetMapping("/all-history")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<BorrowResponse>> getAllHistory() {
        return ApiResponse.<List<BorrowResponse>>builder()
                .code(1000)
                .message("Lấy toàn bộ lịch sử thành công")
                // Gọi service để lấy dữ liệu
                .result(borrowService.getAllHistory())
                .build();
    }
    @PostMapping("/{recordId}/lost")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BorrowResponse> reportLost(@PathVariable Long recordId) {
        return ApiResponse.<BorrowResponse>builder()
                .code(1000)
                .message("Đã ghi nhận trạng thái mất sách")
                // Gọi service xử lý logic mất sách
                .result(borrowService.reportLost(recordId))
                .build();
    }
}
