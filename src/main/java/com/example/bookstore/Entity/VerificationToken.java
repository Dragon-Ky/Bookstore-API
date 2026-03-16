package com.example.bookstore.Entity;

import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "password_reset_token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String otp;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    AppUser user;

    @Column(nullable = false)
    LocalDateTime expiryDate;

    @Column(name = "created_at")
    LocalDateTime createdAt; // Trường này dùng để check 60s

    // Thêm Constructor này
    public VerificationToken(AppUser user) {
        this.user = user;
    }

    // Hàm kiểm tra token hết hạn chưa
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    public void validate(String email) {
        if (!this.user.getEmail().equals(email)) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }
        if (this.isExpired()) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }
    }
}
