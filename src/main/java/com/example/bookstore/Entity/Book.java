package com.example.bookstore.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "Book")
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

    @Lob // Đánh dấu đây là đối tượng lớn
    @Column(name = "image", columnDefinition = "LONGTEXT")
    String image;

    String title;
    String author;
    String category;
    @Column(columnDefinition = "TEXT")
    String description;

    int totalQuantity;

    // Nếu chưa có tính năng mượn/trả, field này có thể coi là dư lúc khởi tạo
    int availableQuantity;

    @Builder.Default // Để Builder không ghi đè giá trị mặc định này
    boolean isDeleted = false;
}
