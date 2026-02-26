package com.example.bookstore.Exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(1000, "Thành công"),
    INVALID_KEY(1001, "Mã lỗi không hợp lệ"),

    EMAIL_EXISTED(2001, "Email đã tồn tại"),
    INVALID_EMAIL_FORMAT(2002,"Vui lòng nhập đúng email"),
    EMAIL_NOT_BLANK(2003,"Email không được để trống"),
    NAME_NOT_BLANK(2004,"Tên không được để trống"),
    PASSWORD_INVALID(2004, "Mật khẩu không được để trống và phải có ít nhất 8 ký tự"),
    PASSWORD_TOO_WEAK(2005, "Mật khẩu phải bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt"),
    AGE_NOT_NULL(2006,"Vui lòng nhập tuổi"),
    AGE_INVALID(2007, "Tuổi phải nằm trong khoảng từ 5 đến 100"),
    EMAIL_NOT_EXISTED(2008,"Email không tồn tại"),
    WRONG_PASSWORD(2009,"Sai mật khẩu"),

    BOOK_NOT_EXISTED(3001, "Cuốn sách này không tồn tại trong thư viện"),
    INVALID_TITLE(3002, "Tên sách phải có ít nhất 3 ký tự"),
    QUANTITY_INVALID(3003, "Số lượng sách không được nhỏ hơn 1"),
    NOT_BLANK_TITLE(3003,"Tên sách ko đc để trống"),
    NOT_BLANK_AUTHOR(3004,"Tên tác giả là bắt buộc"),
    NOT_NULL_CATEGORY(3005,"Phải chọn danh mục cho sách"),
    BOOK_NOT_FOUND(3006,"Không tìm thấy sách theo yêu cầu"),

    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định")
    ;
    private int code;
    private String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
