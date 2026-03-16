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

        VerificationToken token = tokenRepository.findByUserId(user.getId()).orElse(null);

        if (token != null) {
            validateRateLimit(token);
            tokenRepository.delete(token);
        }

        String otp = generateOtp();

        VerificationToken newToken = new VerificationToken(user);
        newToken.setOtp(otp);
        newToken.setCreatedAt(LocalDateTime.now());
        newToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));

        tokenRepository.save(newToken);

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
