package com.example.bookstore.Service.Email;

public interface EmailService {
    void sendVerificationEmail(String to, String otp);
}
