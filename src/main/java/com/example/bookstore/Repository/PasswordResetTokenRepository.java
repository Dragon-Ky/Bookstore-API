package com.example.bookstore.Repository;

import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    Optional<PasswordResetToken> findByOtp(String otp);

    Optional<PasswordResetToken> findByUser(AppUser user);

    void deleteByUser(AppUser user);
}
