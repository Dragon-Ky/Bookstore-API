package com.example.bookstore.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data //gồm tất cả hàm geter ,seter,...
@Builder //khởi tạo đối tượng theo phong cách "lắp ghép"
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // tự thêm private vào biến của bạn
@JsonInclude(JsonInclude.Include.NON_NULL)// Chỉ hiện những trường nào có dữ liệu
public class ApiResponse <T>{
     int code;
     String message;
     T result;
}
