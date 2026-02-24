package com.example.bookstore.Exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(1000, "Thành công"),
    INVALID_KEY(1001, "Mã lỗi không hợp lệ"),

    USER_EXISTED(2001, "Người dùng đã tồn tại"),

    BOOK_NOT_EXISTED(3001, "Cuốn sách này không tồn tại trong thư viện"),
    INVALID_TITLE(3002, "Tên sách phải có ít nhất 3 ký tự"),
    QUANTITY_INVALID(3003, "Số lượng sách không được nhỏ hơn 1"),
    NOT_BLANK_TITLE(3003,"Tên sách ko đc để trống"),
    NOT_BLANK_AUTHOR(3004,"Tên tác giả là bắt buộc"),
    NOT_NULL_CATEGORY(3005,"Phải chọn danh mục cho sách"),

    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định")
    ;
    private int code;
    private String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
