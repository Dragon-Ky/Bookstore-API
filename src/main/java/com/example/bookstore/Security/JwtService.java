package com.example.bookstore.Security;

import com.example.bookstore.Entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor //tạo constructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class JwtService {
    JwtProperties jwtProperties;
    // hàm tạo key token
    private SecretKey getSigningKey(){
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);

    }
    public String generateToken(AppUser user){
        //1.dùng Instant thay cho date
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtProperties.getExpirationMs());

        return Jwts.builder() //bắt đầu build token
                .subject(user.getEmail())  //setSubject = name
                .issuedAt(Date.from(now)) // thời gian hiện tại
                .expiration(Date.from(expiry)) // thời gian hết hạn
                .claim("roles",user.getRole())
                .signWith(getSigningKey())// Bản mới tự nhận diện thuật toán từ Key, không cần HS256
                .compact();
    }
    public Claims extractAllClaims(String token){ //Giải mã token + lấy toàn bộ thông tin (claims) bên trong
        return Jwts.parser() // 1. Dùng .parser() thay cho .parserBuilder()
                .verifyWith(getSigningKey()) // 2. Dùng .verifyWith() thay cho .setSigningKey()
                .build()
                .parseSignedClaims(token)// 3. Dùng .parseSignedClaims() thay cho .parseClaimsJws()
                .getPayload();// 4. Dùng .getPayload() thay cho .getBody()
    }
    // kiểm tra token này của ai
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }
    // kiểm tra token này còn hạn sử dụng ko
    public boolean isTokenValid(String token){
        try {
            Claims claims=extractAllClaims(token); // lấy móc thời gian hết hạn token
            return claims.getExpiration().toInstant().isAfter(Instant.now()); // so sánh với thời gian hiện tại
        }catch (Exception e){
            return false; // hết hạn hoặc sai token thì sẽ ném ra
        }
    }
}
