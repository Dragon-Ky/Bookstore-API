package com.example.bookstore.Service.User;

import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.ENUM.Type;
import com.example.bookstore.Entity.VerificationToken;
import com.example.bookstore.Repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final PasswordResetTokenRepository tokenRepository;
    public String createAndSaveOtp(AppUser user, Type type) {
        String token;
        LocalDateTime expiry;

        // Nếu là đăng ký: tạo chuỗi UUID dài (an toàn cho Link), hết hạn sau 24h
        if (type == Type.REGISTRATION) {
            token = UUID.randomUUID().toString();
            expiry = LocalDateTime.now().plusMinutes(5);
        }
        // Nếu là reset mật khẩu: tạo 6 số ngẫu nhiên, hết hạn sau 5 phút
        else {
            token = String.valueOf(new Random().nextInt(900000) + 100000);
            expiry = LocalDateTime.now().plusMinutes(5);
        }

        // Xóa token cũ của user nếu có trước khi tạo mới (để tránh rác DB)
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setOtp(token); // Trong DB field vẫn là otp nhưng ta lưu cả token vào đây
        verificationToken.setUser(user);
        verificationToken.setType(type);
        verificationToken.setExpiryDate(expiry);

        tokenRepository.save(verificationToken);
        return token;
    }
}
