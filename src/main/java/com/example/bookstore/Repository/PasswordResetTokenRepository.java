package com.example.bookstore.Repository;

import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<VerificationToken,Long> {
    Optional<VerificationToken> findByOtp(String otp);

    Optional<VerificationToken> findByUser(AppUser user);

    void deleteByUser(AppUser user);
}
