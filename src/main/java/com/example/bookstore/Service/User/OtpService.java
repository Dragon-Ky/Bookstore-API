package com.example.bookstore.Service;

import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.PasswordResetToken;
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
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // Tạo mã 6 số

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setOtp(otp);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));

        tokenRepository.save(resetToken);
        return otp;
    }
}
