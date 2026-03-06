package com.example.bookstore.Entity;

import com.example.bookstore.Entity.ENUM.BorrowStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_records")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    Book book;

    @Column(nullable = false)
    LocalDateTime borrowDate;

    @Column(nullable = false)
    LocalDateTime dueDate;

    @Column(name = "return_date", nullable = true)
    LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    BorrowStatus status = BorrowStatus.BORROWING;
}
