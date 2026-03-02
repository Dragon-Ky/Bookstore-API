package com.example.bookstore.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String token;
    boolean authenticated;
    String role;

    public static LoginResponse of(String token ,String role){
        return LoginResponse.builder()
                .token(token)
                .authenticated(true)
                .role(role)
                .build();
    }
}
