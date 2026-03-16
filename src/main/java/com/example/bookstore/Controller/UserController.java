package com.example.bookstore.Controller;

import com.example.bookstore.DTO.ApiResponse;
import com.example.bookstore.DTO.Request.AuthenticationRequest;
import com.example.bookstore.DTO.Request.Creation.UserCreationRequest;
import com.example.bookstore.DTO.Request.ResetPasswordRequest;
import com.example.bookstore.DTO.Response.LoginResponse;
import com.example.bookstore.DTO.Response.UserResponse;

import com.example.bookstore.Service.User.AuthenticationService;
import com.example.bookstore.Service.User.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://front-end-library.onrender.com")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
    UserService userService;
    AuthenticationService authenticationService;

    // Kích hoạt tài khoản qua Link
    @PostMapping("/verify-otp")
    public ApiResponse<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {

        return ApiResponse.<String>builder()
                .code(1000)
                .result(authenticationService.verifyEmail(email, otp))
                .message("Xác thực thành công")
                .build();
    }
    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<String>builder()
                .code(1000)
                .result(userService.register(request))
                .message("Vui lòng kiểm tra email để kích hoạt tài khoản")
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


    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestParam String email) {

        return ApiResponse.<String>builder()
                .code(1000)
                .result(authenticationService.forgotPassword(email))
                .message("Thực hiện thay đổi mật khẩu")
                .build();
    }
    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        // Gọi Service xử lý kiểm tra OTP và đổi mật khẩu
        authenticationService.resetPassword(request);

        return ApiResponse.<String>builder()
                .code(1000)
                .result("Mật khẩu đã được thay đổi thành công!")
                .build();
    }
    @DeleteMapping("/cleanup")
    public String cleanup() {
        userService.deleteUserFalse();
        return "Đã dọn dẹp sạch tài khoản ảo!";
    }
    @PostMapping("/resend-verification")
    public ApiResponse<String> resendVerification(@RequestParam String email) {

        authenticationService.resendVerification(email);

        return ApiResponse.<String>builder()
                .code(1000)
                .result("Mã xác thực mới đã được gửi đến email của bạn.")
                .build();
    }
}
