package com.example.bookstore.Security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component //giống bean cũng quản lý hộ bới spring
@ConfigurationProperties(prefix = "app.jwt")
@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtProperties {
    String secret;
    long expirationMs;
}
