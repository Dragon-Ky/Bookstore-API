package com.example.bookstore.Service.User;

import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.ENUM.Type;
import com.example.bookstore.Entity.VerificationToken;
import com.example.bookstore.Repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final PasswordResetTokenRepository tokenRepository;
    public String createAndSaveOtp(AppUser user, Type type) {
        // 1. Lấy token cũ (nếu có)
        VerificationToken token = tokenRepository.findByUserId(user.getId())
                .orElse(new VerificationToken(user));

        // 2. Kiểm tra chống spam
        validateRateLimit(token);

        // 3. Tạo và cập nhật thông tin OTP
        String otp = generateNumericOtp();
        updateTokenDetails(token, otp, type);

        // 4. Lưu và trả về
        tokenRepository.save(token);
        return otp;
    }
    private void validateRateLimit(VerificationToken token) {
        if (token.getId() == null) return; // Token mới tinh, không cần check spam

        LocalDateTime lastSentTime = token.getExpiryDate().minusMinutes(5);
        LocalDateTime nextAllowedTime = lastSentTime.plusSeconds(60);

        if (nextAllowedTime.isAfter(LocalDateTime.now())) {
            long secondsToWait = java.time.Duration.between(LocalDateTime.now(), nextAllowedTime).getSeconds();
            throw new RuntimeException("Vui lòng đợi " + secondsToWait + " giây.");
        }
    }

    /**
     * Tạo mã số ngẫu nhiên 6 chữ số.
     * Tách riêng để sau này có thể đổi sang mã phức tạp hơn mà không sửa logic chính.
     */
    private String generateNumericOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    /**
     *  Cập nhật các trạng thái mới cho Entity.
     */
    private void updateTokenDetails(VerificationToken token, String otp, Type type) {
        token.setOtp(otp);
        token.setType(type);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(5));
    }
}
