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
import com.example.bookstore.Repository.UserRepository;
import com.example.bookstore.Security.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // khai báo khỏi viết hàm khởi tạo
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    JwtService jwtService;
    //hàm kiểu tra email tồn tại ko
    private void checkEmailExit(String email){
        if (userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
    }
    public UserResponse register(UserCreationRequest request){
        //1. kiểm tra email đã tồn tại chưa
        checkEmailExit(request.getEmail());
        //2. Map dữ liệu từ Request sang Entity
        AppUser user = userMapper.toAppUser(request);

        //3.mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        //4.mặc định khi đăng ký là là ROLE_USER
        user.setRole(Role.USER);

        //5.Lưu va trả về
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public LoginResponse login(AuthenticationRequest request){
        //1. kiểm tra email
        AppUser user = userRepository.findByEmailOrThrow(request.getEmail());
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
}
