package com.example.bookstore.Service.User;

import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.VerificationToken;
import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import com.example.bookstore.Repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final PasswordResetTokenRepository tokenRepository;
    public String createAndSaveOtp(AppUser user) {
        // 1. Lấy token cũ của User từ DB
        // Nếu có id -> Hibernate sẽ UPDATE. Nếu null -> Hibernate sẽ INSERT.
        VerificationToken token = tokenRepository.findByUserId(user.getId())
                .orElseGet(() -> new VerificationToken(user));

        // 2. Kiểm tra chống spam (Phải check trên trường createdAt mới thêm)
        validateRateLimit(token);

        // 3. Cập nhật thông tin mới
        String otp = generateOtp();
        token.setOtp(otp);

        token.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        token.setCreatedAt(LocalDateTime.now()); // Reset mốc 60s mỗi lần gửi thành công

        // 4. Lưu xuống DB
        tokenRepository.save(token);

        return otp;
    }

// --- Các hàm hỗ trợ ---

    private void validateRateLimit(VerificationToken token) {
        if (token.getCreatedAt() != null) {
            LocalDateTime nextAllowedTime = token.getCreatedAt().plusMinutes(1);
            if (LocalDateTime.now().isBefore(nextAllowedTime)) {
                // Nếu chưa đủ 60s, ném lỗi ngay để báo về Frontend
                throw new AppException(ErrorCode.QUICK_RETRY);
            }
        }
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
