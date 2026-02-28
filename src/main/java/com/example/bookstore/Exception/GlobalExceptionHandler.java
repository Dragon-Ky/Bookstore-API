package com.example.bookstore.Exception;

import com.example.bookstore.DTO.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // có bao nhiêu lỗi đem về đây xử lý
public class GlobalExceptionHandler {

    // 1. Bắt lỗi do mình tự định nghĩa (AppException)
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<?>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode(); // Lấy ErrorCode từ lỗi bị ném ra

        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode()); // Truyền mã 1001, 1002... vào đây
        apiResponse.setMessage(errorCode.getMessage()); // Truyền tin nhắn tương ứng vào

        return ResponseEntity.badRequest().body(apiResponse);
    }

    // 2. Bắt các lỗi hệ thống khác (Ví dụ: Server sập, lỗi code...)
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<?>> handlingRuntimeException(Exception exception) { // Đổi thành Exception
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage() + ": " + exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handlingValidation(MethodArgumentNotValidException exception) {
        // 1. Lấy cái key message bạn đặt ở BookRequest (ví dụ: "INVALID_TITLE")
        String enumKey = exception.getFieldError().getDefaultMessage();

        // 2. Tìm ErrorCode tương ứng với key đó, nếu không thấy thì dùng INVALID_KEY
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            // Giữ nguyên INVALID_KEY nếu không map được enum
        }

        // 3. Trả về response theo chuẩn
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
