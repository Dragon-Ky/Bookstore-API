package com.example.bookstore.Repository;

import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);

    default AppUser findByEmailOrThrow(String email){
        return findByEmail(email)
                .orElseThrow(()->new AppException(ErrorCode.EMAIL_NOT_EXISTED));
    }
}
