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

    public void sendVerificationEmail(String toEmail, String token, Type type) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);

            if (type == Type.REGISTRATION) {
                helper.setSubject("Kích hoạt tài khoản Bookstore");
                String url = "http://localhost:8080/api/v1/users/verify?token=" + token + "&email=" + toEmail;

                // Đây là đoạn mã tạo Nút bấm bằng HTML
                String htmlContent = "<h1>Chào mừng bạn!</h1>" +
                        "<p>Vui lòng nhấn vào nút bên dưới để kích hoạt tài khoản:</p>" +
                        "<a href='" + url + "' style='background-color: #4CAF50; color: white; padding: 14px 25px; " +
                        "text-align: center; text-decoration: none; display: inline-block; border-radius: 8px; font-weight: bold;'>" +
                        "KÍCH HOẠT TÀI KHOẢN</a>" +
                        "<p>Nếu nút không hoạt động, vui lòng bỏ qua email này.</p>";

                helper.setText(htmlContent, true); // 'true' để Java hiểu đây là HTML
            } else {
                // Logic cho OTP 6 số giữ nguyên vì reset mật khẩu cần nhập số vào App
                helper.setSubject("Mã đặt lại mật khẩu");
                helper.setText("Mã OTP của bạn là: " + token, false);
            }
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi gửi mail");
        }
    }

}
