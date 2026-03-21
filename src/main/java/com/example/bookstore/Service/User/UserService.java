package com.example.bookstore.Service.User;

import com.example.bookstore.DTO.Request.AuthenticationRequest;
import com.example.bookstore.DTO.Request.Creation.UserCreationRequest;
import com.example.bookstore.DTO.Response.LoginResponse;
import com.example.bookstore.DTO.Response.UserResponse;
import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.ENUM.Role;
import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import com.example.bookstore.Mapper.UserMapper;
import com.example.bookstore.Repository.PasswordResetTokenRepository;
import com.example.bookstore.Repository.UserRepository;
import com.example.bookstore.Security.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor // khai báo khỏi viết hàm khởi tạo
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    JwtService jwtService;
    OtpService otpService;
    EmailService emailService;
    PasswordResetTokenRepository tokenRepository;
    //hàm kiểu tra email tồn tại ko
    private void checkEmailExit(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            if (Boolean.TRUE.equals(user.getIsActive())) {
                // Nếu đã active thì tuyệt đối không cho đăng ký trùng
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            } else {
                // Nếu chưa active:
                // 1. Xóa các token liên quan trước để không bị lỗi khóa ngoại
                tokenRepository.deleteByUser(user);
                // 2. Xóa user cũ
                userRepository.delete(user);
                // 3. Flush để DB sạch bóng email này trước khi lưu mới
                userRepository.flush();
            }
        });
    }
    @Transactional
    public String register(UserCreationRequest request){
        //1. kiểm tra email đã tồn tại chưa
        checkEmailExit(request.getEmail());
        //2. Map dữ liệu từ Request sang Entity
        AppUser user = userMapper.toAppUser(request);

        //3.mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // chưa xác thực email
        user.setIsActive(false);

        //4.mặc định khi đăng ký là là ROLE_USER
        user.setRole(Role.USER);
        userRepository.save(user);
        // tạo otp
        String otp = otpService.createAndSaveOtp(user);
        // gửi gmail
        try {
            emailService.sendVerificationEmail(user.getEmail(), otp);
        } catch (Exception e) {
            throw new AppException(ErrorCode.OTP_NOT_FOUND);
        }

        //5.Lưu va trả về
        return "OTP_SENT";
    }
    public LoginResponse login(AuthenticationRequest request){
        //1. kiểm tra email
        AppUser user = userRepository.findByEmailOrThrow(request.getEmail());
        // kiểm tra active
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new AppException(ErrorCode.USER_NOT_ACTIVE);
        }
        //2. kiểm tra mật khẩu
        boolean authenticated = passwordEncoder.matches(request.getPassword(),user.getPassword());
        if (!authenticated){
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
        //3.Tạo token
        String token = jwtService.generateToken(user);
        //4.trả về Response
        return LoginResponse.of(token,user.getRole().name());
    }
    public UserResponse getUserById(Long userId){
        AppUser user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }
    public UserResponse deleteUserFalse(){
        return userRepository.deleteAllByIsActiveFalse();
    }
}
