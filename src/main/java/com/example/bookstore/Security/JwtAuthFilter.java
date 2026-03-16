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
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        // 1. Nếu không có Header hoặc không bắt đầu bằng Bearer -> Cho đi tiếp ngay
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            // 2. Trích xuất username. Nếu Token hết hạn, dòng này sẽ ném Exception.
            final String username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // 3. Kiểm tra tính hợp lệ của Token
                if (jwtService.isTokenValid(jwt)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // 4. CHỐT CHẶN QUAN TRỌNG:
            // Khi lỗi (hết hạn, sai định dạng...), ta chỉ ghi log.
            // Tuyệt đối KHÔNG ném Exception ra ngoài khối này.
            log.error("JWT Security Error: {}", e.getMessage());

            // Bạn có thể xóa SecurityContext để đảm bảo an toàn nếu cần
            SecurityContextHolder.clearContext();
        }

        // 5. LUÔN LUÔN gọi doFilter ở cuối cùng để request không bị treo
        filterChain.doFilter(request, response);
    }
}
