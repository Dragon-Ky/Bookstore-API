package com.example.bookstore.Service.User;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        String subject = "Mã xác thực đổi mật khẩu";
        String body = String.format("Mã OTP của bạn là: %s\nHiệu lực trong 5 phút.", otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}
