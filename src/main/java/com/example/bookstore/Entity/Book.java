package com.example.bookstore.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter // Hoặc đặt Setter riêng cho từng field bên dưới
@NoArgsConstructor // Cần thiết cho JPA
@Builder // Giúp bạn tạo object sạch hơn
@AllArgsConstructor // Cần cho @Builder hoạt động
@SQLRestriction("is_deleted = false") // Luôn chỉ lấy những bản ghi chưa xóa
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;
    String author;
    String category;

    int totalQuantity;

    // Nếu chưa có tính năng mượn/trả, field này có thể coi là dư lúc khởi tạo
    int availableQuantity;

    @Builder.Default // Để Builder không ghi đè giá trị mặc định này
    boolean isDeleted = false;
}
