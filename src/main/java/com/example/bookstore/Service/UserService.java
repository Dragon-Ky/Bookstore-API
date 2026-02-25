package com.example.bookstore.Service;

import com.example.bookstore.DTO.Request.Creation.UserCreationRequest;
import com.example.bookstore.DTO.Response.UserResponse;
import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.ENUM.Role;
import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import com.example.bookstore.Mapper.UserMapper;
import com.example.bookstore.Repository.UserRepository;
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
}
