package com.example.bookstore.Service.Email;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile("prod") // Chỉ chạy khi cấu hình SPRING_PROFILES_ACTIVE=prod
public class ResendEmailService implements EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Override
    public void sendVerificationEmail(String to, String otp) {
        Resend resend = new Resend(resendApiKey);

        // 1. Đồng bộ giao diện HTML giống hệt GmailEmailService
        String title = "MÃ XÁC THỰC";
        String htmlContent =
                "<div style='font-family: Arial; text-align: center; border: 1px solid #ddd; padding: 20px;'>" +
                        "<h3>" + title + "</h3>" +
                        "<p>Vui lòng nhập mã dưới đây vào ứng dụng của bạn:</p>" +
                        "<div style='background: #eee; padding: 20px; font-size: 30px; font-weight: bold; letter-spacing: 5px;'>" +
                        otp +
                        "</div>" +
                        "<p>Mã có hiệu lực trong 5 phút.</p>" +
                        "</div>";

        // 2. Cấu hình gửi qua Resend API
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Bookstore <onboarding@resend.dev>") // Giữ nguyên domain mặc định nếu chưa verify domain riêng
                .to(to)
                .subject("[Bookstore] " + title)
                .html(htmlContent) // Gửi chuỗi HTML vừa tạo
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            log.info("Gửi qua Resend thành công! ID: " + data.getId());
        } catch (Exception e) {
            log.error("Lỗi gửi mail Resend: " + e.getMessage());
            throw new RuntimeException("Resend API error: " + e.getMessage());
        }
    }
}