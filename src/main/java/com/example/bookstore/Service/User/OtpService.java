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
        // Tạo mã 6 số ngẫu nhiên từ 100000 đến 999999
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        VerificationToken token = new VerificationToken();
        token.setOtp(otp);
        token.setUser(user);
        token.setType(type);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(5)); // Hết hạn sau 5 phút

        tokenRepository.save(token);
        return otp;
    }
}
