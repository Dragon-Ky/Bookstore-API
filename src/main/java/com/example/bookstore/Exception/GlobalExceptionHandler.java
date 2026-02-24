package com.example.bookstore.Exception;

import com.example.bookstore.DTO.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // có bao nhiêu lỗi đem về đây xử lý
public class GlobalExceptionHandler {

    // 1. Bắt lỗi do mình tự định nghĩa (AppException)
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode(); // Lấy ErrorCode từ lỗi bị ném ra

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode()); // Truyền mã 1001, 1002... vào đây
        apiResponse.setMessage(errorCode.getMessage()); // Truyền tin nhắn tương ứng vào

        return ResponseEntity.badRequest().body(apiResponse);
    }

    // 2. Bắt các lỗi hệ thống khác (Ví dụ: Server sập, lỗi code...)
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(9999); // Mã mặc định cho lỗi chưa xác định
        apiResponse.setMessage("Lỗi hệ thống: " + exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }
}
