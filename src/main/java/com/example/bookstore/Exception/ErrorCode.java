package com.example.bookstore.Exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // --- 1xxx: Hệ thống & Xác thực ---
    SUCCESS(1000, "Thành công", HttpStatus.OK),
    INVALID_KEY(1001, "Mã lỗi không hợp lệ", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1002, "Lỗi xác thực", HttpStatus.UNAUTHORIZED),
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),

    // --- 2xxx: Lỗi liên quan đến User & Validation ---
    EMAIL_EXISTED(2001, "Email đã tồn tại", HttpStatus.CONFLICT),
    INVALID_EMAIL_FORMAT(2002, "Vui lòng nhập đúng email", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_BLANK(2003, "Email không được để trống", HttpStatus.BAD_REQUEST),
    NAME_NOT_BLANK(2004, "Tên không được để trống", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_WEAK(2005, "Mật khẩu phải bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt", HttpStatus.BAD_REQUEST),
    AGE_NOT_NULL(2006, "Vui lòng nhập tuổi", HttpStatus.BAD_REQUEST),
    AGE_INVALID(2007, "Tuổi phải nằm trong khoảng từ 5 đến 100", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED(2008, "Email không tồn tại", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD(2009, "Sai mật khẩu hoặc tài khoản", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(2010, "User không tồn tại", HttpStatus.NOT_FOUND),
    PASSWORD_INVALID(2011, "Mật khẩu không được để trống và phải có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
    NOT_LOGIN(2012,"Người dùng chưa đăng nhập hoặc phiên làm việc đã hết hạn",HttpStatus.NOT_FOUND),

    // --- 3xxx: Lỗi liên quan đến Book ---
    BOOK_NOT_EXISTED(3001, "Cuốn sách này không tồn tại trong thư viện", HttpStatus.NOT_FOUND),
    INVALID_TITLE(3002, "Tên sách phải có ít nhất 3 ký tự", HttpStatus.BAD_REQUEST),
    QUANTITY_INVALID(3003, "Số lượng sách không được nhỏ hơn 1", HttpStatus.BAD_REQUEST),
    NOT_BLANK_TITLE(3004, "Tên sách không được để trống", HttpStatus.BAD_REQUEST), // Đổi từ 3003 thành 3004
    NOT_BLANK_AUTHOR(3005, "Tên tác giả là bắt buộc", HttpStatus.BAD_REQUEST),   // Đổi từ 3004 thành 3005
    NOT_NULL_CATEGORY(3006, "Phải chọn danh mục cho sách", HttpStatus.BAD_REQUEST), // Đổi từ 3005 thành 3006
    BOOK_NOT_FOUND(3007, "Không tìm thấy sách theo yêu cầu", HttpStatus.NOT_FOUND), // Đổi từ 3006 thành 3007
    NOT_BLANK_IMG(3008,"Ảnh không được để trống",HttpStatus.NOT_FOUND),
    BOOK_ID_REQUIRED(3009,"Id sách không tồn tại",HttpStatus.NOT_FOUND),
    BOOK_OUT_OF_STOCK(3010,"Sách đã hết hàng" ,HttpStatus.BAD_REQUEST ),
    MAX_BORROW_REACHED(3011,"Bạn đã đạt giới hạn mượn sách" ,HttpStatus.CONFLICT ),
    NOT_BLANK_DESCRIPTION(3012,"không được để tiêu đề trống",HttpStatus.NOT_FOUND),
    BOOK_ALREADY_BORROWED(3013,"Sách này bạn đã mượn",HttpStatus.CONFLICT),

    RECORDID_NOT_FOUND(4001,"Id giao dịch ko tồn tại" ,HttpStatus.NOT_FOUND ),
    BOOK_ALREADY_RETURNED(4002,"Sách đã được trả về thư viện" ,HttpStatus.CONFLICT ),
    INVALID_STATUS_CHANGE(4003,"Task đã được chỉnh sữa" ,HttpStatus.CONFLICT );

    int code;
    String message;
    HttpStatusCode statusCode;
    ErrorCode(int code, String message,HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode= statusCode;
    }
}
