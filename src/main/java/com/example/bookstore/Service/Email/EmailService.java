package com.example.bookstore.Service.Email;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendVerificationEmail(String to, String otp);
}
