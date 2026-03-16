package com.example.bookstore.Entity;

import com.example.bookstore.Entity.ENUM.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "app_users")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String email;

    String name;
    String password;
    int age;

    @Enumerated(EnumType.STRING)
    Role role;

    @Column(name = "is_active")
    Boolean isActive = false;

    public boolean getIsActive() {
        return isActive != null && isActive;
        // Nếu isActive là null hoặc false thì đều trả về false
    }
}
