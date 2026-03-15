package com.example.bookstore.DTO.Request;

import lombok.AccessLevel;
import lombok.Data;

import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {
    String email;
    String otp;
    String newPassword;
}
