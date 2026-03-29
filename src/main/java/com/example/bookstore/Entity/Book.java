package com.example.bookstore.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
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
    // mysql @Column(name = "image", columnDefinition = "LONGTEXT")
    @Column(name = "image", columnDefinition = "TEXT") //post
    String image;

    @Column(columnDefinition = "TEXT")
    String title;

    @Column(columnDefinition = "TEXT")
    String author;

    @ElementCollection
    Set<String> categories = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    String description;

    int totalQuantity;

    // Nếu chưa có tính năng mượn/trả, field này có thể coi là dư lúc khởi tạo
    int availableQuantity;

    @Builder.Default // Để Builder không ghi đè giá trị mặc định này
    boolean isDeleted = false;
}
