package com.example.bookstore.Service.User;

import com.example.bookstore.DTO.Request.ResetPasswordRequest;
import com.example.bookstore.Entity.AppUser;

import com.example.bookstore.Entity.VerificationToken;
import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;

import com.example.bookstore.Repository.PasswordResetTokenRepository;
import com.example.bookstore.Repository.UserRepository;
import com.example.bookstore.Security.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor // dùng để khỏi viết this.
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    JwtService jwtService;
    EmailService emailService;
    OtpService otpService;
    PasswordResetTokenRepository tokenRepository;

    public void resendVerification(String email) {
        // 1. Tìm User đã tồn tại trong DB
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 2. Gọi hàm logic tạo và lưu OTP đã tách sạch ở các bước trước
        // Hàm này sẽ tự check 60s và tự động "đè" (Update) OTP cũ
        String newOtp = otpService.createAndSaveOtp(user);

        // 3. Gửi email
        emailService.sendVerificationEmail(user.getEmail(), newOtp);
    }

    public String forgotPassword(String email) {
        //check email có tồn tại ko
        AppUser user = userRepository.findByEmailOrThrow(email);
        // tạo token ngẫu nhiên và lưu
        String otp = otpService.createAndSaveOtp(user);
        //gửi gmail
        emailService.sendVerificationEmail(user.getEmail(), otp);
        return "Mã OTP đã được gửi vào Email của bạn.";
    }

    private VerificationToken getValidToken(String otp) {
        return tokenRepository.findByOtp(otp)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        //tìm otp
        VerificationToken resetToken = getValidToken(request.getOtp());
        // để entity tự check
        resetToken.validate(request.getEmail());
        // đổi mật khẩu
        AppUser user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        //xóa token
        tokenRepository.delete(resetToken);
    }

    @Transactional
    public String verifyEmail(String email, String otp) {
        // 1. Tìm mã trong DB
        VerificationToken token = tokenRepository.findByOtp(otp)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));

        // 2. Kiểm tra email và hết hạn (Dùng hàm validate  trong Entity)
        token.validate(email);

        // 3. Nếu là đăng ký thì set active = true

        AppUser user = token.getUser();
        user.setIsActive(true);
        userRepository.save(user);

        // 4. Xóa mã sau khi dùng
        tokenRepository.delete(token);
        return "đã check";
    }
}
