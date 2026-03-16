package com.example.bookstore.Service.User;

import com.example.bookstore.DTO.Request.AuthenticationRequest;

import com.example.bookstore.DTO.Request.ResetPasswordRequest;
import com.example.bookstore.DTO.Response.LoginResponse;
import com.example.bookstore.Entity.AppUser;

import com.example.bookstore.Entity.ENUM.Type;
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

    public LoginResponse authenticate(AuthenticationRequest request) {
        //1. check email có tồn tại ko
        var user = userRepository.findByEmailOrThrow(request.getEmail());
        //2.Kiểm tra mật khẩu (Sử dụng matches để so sánh pass đã mã hóa)
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
        String token = jwtService.generateToken(user);
        //3. nếu đúng thì trả token
        return LoginResponse.of(token, user.getRole().name());
    }

    public String forgotPassword(String email) {
        //check email có tồn tại ko
        AppUser user = userRepository.findByEmailOrThrow(email);
        // tạo token ngẫu nhiên và lưu
        String otp = otpService.createAndSaveOtp(user,Type.PASSWORD_RESET);
        //gửi gmail
        emailService.sendVerificationEmail(user.getEmail(), otp,Type.PASSWORD_RESET);
        return "Mã OTP đã được gửi vào Email của bạn.";
    }

    private VerificationToken getValidToken(String otp) {
        return tokenRepository.findByOtp(otp)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));
    }
    @Transactional
    public void resetPassword(ResetPasswordRequest request){
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
    public void verifyEmail(String email, String tokenValue) {
        // 1. Tìm token dựa trên giá trị chuỗi (OTP/UUID)
        VerificationToken token = tokenRepository.findByOtp(tokenValue)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));

        // 2. Sử dụng hàm validate có sẵn trong Entity VerificationToken để check email & expiry
        token.validate(email);

        // 3. Kiểm tra xem token này có đúng là loại REGISTRATION không
        if (token.getType() != Type.REGISTRATION) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        // 4. Kích hoạt user
        AppUser user = token.getUser();
        user.setActive(true);
        userRepository.save(user);

        // 5. Xóa token sau khi dùng
        tokenRepository.delete(token);
    }
}
