package com.example.bookstore.DTO.Request.Creation;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data // gồm tất cả hàm lấy dữ liệu
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "EMAIL_NOT_BLANK")
    @Email(message = "INVALID_EMAIL_FORMAT")
    String email;

    @NotBlank(message = "NAME_NOT_BLANK")
    String name;

    @NotBlank(message = "PASSWORD_INVALID")
    @Size(min = 8, message = "PASSWORD_INVALID") // Ít nhất 8 ký tự
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "PASSWORD_TOO_WEAK"
    )
    String password;

    @NotNull(message = "AGE_NOT_NULL")
    @Min(value = 5, message = "AGE_INVALID")
    @Max(value = 100, message = "AGE_INVALID")
    int age;

}
