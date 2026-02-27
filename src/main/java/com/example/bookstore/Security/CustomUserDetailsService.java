package com.example.bookstore.Security;

import com.example.bookstore.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class CustomUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        var user = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("ko tìm thấy người dùng"));
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name()) // Trả về role ADMIN/USER
                .build();
    }
}
