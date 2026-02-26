package com.example.bookstore.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor //tạo constructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    JwtService jwtService;
    UserDetailsService userDetailsService;
    //OncePerRequestFilter: Đảm bảo Filter này chỉ chạy duy nhất 1 lần mỗi khi có request gửi đến
    @Override
    protected void doFilterInternal( // Mọi Request (yêu cầu) từ trình duyệt/mobile gửi lên đều phải đi qua đây trước khi vào Controller
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        // kiểm tra có token ko
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        //trích xuất token
        jwt = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwt);
            //xác thực token
            if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if(jwtService.isTokenValid(jwt)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e){
            log.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request,response);
    }
}
