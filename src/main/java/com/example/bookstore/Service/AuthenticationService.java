package com.example.bookstore.Service;

import com.example.bookstore.DTO.Request.AuthenticationRequest;
import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import com.example.bookstore.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // dùng để khỏi viết this.
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    public String authenticate(AuthenticationRequest request){
        //1. check email có tồn tại ko
        var user = userRepository.checkEmailExit(request.getEmail());
        //2.Kiểm tra mật khẩu (Sử dụng matches để so sánh pass đã mã hóa)
        boolean authenticated=passwordEncoder.matches(request.getPassword(),user.getPassword());
        if (!authenticated){
            throw  new AppException(ErrorCode.WRONG_PASSWORD);
        }
        //3. nếu đúng thì trả token
        return generateToken(user);
    }
    private String generateToken(AppUser user) {
        return "fake-token-for-now";
    }

}
