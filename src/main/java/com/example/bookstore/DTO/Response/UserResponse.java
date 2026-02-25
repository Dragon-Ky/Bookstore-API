package com.example.bookstore.DTO.Response;

import com.example.bookstore.Entity.ENUM.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // đa năng
@Builder // tạo chuỗi mới new
@NoArgsConstructor// (Constructor rỗng)
@AllArgsConstructor //(Constructor đầy đủ)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // nếu có null thì bỏ ko lấy
public class UserResponse {
    Long id;
    String email;
    String name;
    String password;
    int age;

    Role role;
}
