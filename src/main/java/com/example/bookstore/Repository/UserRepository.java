package com.example.bookstore.Repository;

import com.example.bookstore.DTO.Response.UserResponse;
import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);

    default AppUser findByEmailOrThrow(String email){
        return findByEmail(email)
                .orElseThrow(()->new AppException(ErrorCode.EMAIL_NOT_EXISTED));
    }
        // Hàm xóa những user chưa kích hoạt
    @Modifying
    @Transactional
    @Query("DELETE FROM AppUser u WHERE u.isActive = false AND u.email = :email")
    void deleteByEmailAndIsActiveFalse(String email);

    UserResponse deleteAllByIsActiveFalse();
}
