package com.example.bookstore.Repository;

import com.example.bookstore.Entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord,Long> {
    List<BorrowRecord> findByUserEmailAndIsReturnedFalse(String email);
}
