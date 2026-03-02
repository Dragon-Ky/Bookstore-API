package com.example.bookstore.Config;

import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.ENUM.Role;
import com.example.bookstore.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE ,makeFinal = true)
public class DataInitializer {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    @PostConstruct
    public void initAdminAccount() {
        String adminEmail = "admin@bookstore.com";

        // Kiểm tra xem admin đã tồn tại chưa để tránh tạo trùng mỗi lần restart
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            AppUser admin = new AppUser();
            admin.setEmail(adminEmail);

            // QUAN TRỌNG: Phải mã hóa mật khẩu trước khi lưu
            admin.setPassword(passwordEncoder.encode("admin123"));

            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
        }
    }

}
