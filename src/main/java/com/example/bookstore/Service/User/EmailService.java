package com.example.bookstore.Service.User;


import com.example.bookstore.Entity.ENUM.Type;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String otp, Type type) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);

            String title = (type == Type.REGISTRATION) ? "MÃ XÁC THỰC ĐĂNG KÝ" : "MÃ ĐẶT LẠI MẬT KHẨU";
            helper.setSubject("[Bookstore] " + title);

            String htmlContent = "<div style='font-family: Arial; text-align: center; border: 1px solid #ddd; padding: 20px;'>" +
                    "<h3>" + title + "</h3>" +
                    "<p>Vui lòng nhập mã dưới đây vào ứng dụng của bạn:</p>" +
                    "<div style='background: #eee; padding: 20px; font-size: 30px; font-weight: bold; letter-spacing: 5px;'>" +
                    otp + "</div>" +
                    "<p>Mã có hiệu lực trong 5 phút.</p>" +
                    "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi gửi mail");
        }
    }
    }
