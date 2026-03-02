package com.example.bookstore.Service.User;

import com.example.bookstore.DTO.Request.AuthenticationRequest;

import com.example.bookstore.DTO.Response.LoginResponse;
import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import com.example.bookstore.Repository.UserRepository;
import com.example.bookstore.Security.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // dùng để khỏi viết this.
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    JwtService jwtService;

    public LoginResponse authenticate(AuthenticationRequest request){
        //1. check email có tồn tại ko
        var user = userRepository.findByEmailOrThrow(request.getEmail());
        //2.Kiểm tra mật khẩu (Sử dụng matches để so sánh pass đã mã hóa)
        boolean authenticated=passwordEncoder.matches(request.getPassword(),user.getPassword());
        if (!authenticated){
            throw  new AppException(ErrorCode.WRONG_PASSWORD);
        }
        String token = jwtService.generateToken(user);
        //3. nếu đúng thì trả token
        return LoginResponse.of(token,user.getRole().name());
    }

}
