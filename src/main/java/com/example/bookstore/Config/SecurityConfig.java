package com.example.bookstore.Config;

import com.example.bookstore.Security.JwtAuthFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // mở file ra đọc liền
@EnableWebSecurity // bật rào chắn bảo mật api
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {
    JwtAuthFilter jwtAuthFilter;
    @Bean //khai báo để khỏi sài new
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf->csrf.disable());//tắt csrf để test API
        // Cấu hình tạm thời: Cho phép tất cả mọi người vào mọi link
        http.authorizeHttpRequests(auth->auth.anyRequest().permitAll()
                .anyRequest().authenticated()
        ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
