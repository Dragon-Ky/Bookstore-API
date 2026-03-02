package com.example.bookstore.Controller;

import com.example.bookstore.DTO.ApiResponse;
import com.example.bookstore.DTO.Request.AuthenticationRequest;
import com.example.bookstore.DTO.Request.Creation.UserCreationRequest;
import com.example.bookstore.DTO.Response.LoginResponse;
import com.example.bookstore.DTO.Response.UserResponse;

import com.example.bookstore.Service.User.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .message("Đăng ký tài khoản thành công!")
                .result(userService.register(request))
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable Long userId) {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .result(userService.getUserById(userId))
                .build();
    }
    @PostMapping("/login")
    public ApiResponse<LoginResponse> loginUser(@RequestBody @Valid AuthenticationRequest request){
        // Gọi login và nhận về token + trạng thái
        LoginResponse result = userService.login(request);

        return ApiResponse.<LoginResponse>builder()
                .code(1000)
                .result(result)
                .message("Chào mừng bạn quay trở lại!")
                .build();
    }
}
