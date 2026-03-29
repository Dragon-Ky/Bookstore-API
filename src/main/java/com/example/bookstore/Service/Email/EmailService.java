package com.example.bookstore.Service.Email;
@Service
public interface EmailService {
    void sendVerificationEmail(String to, String otp);
}
