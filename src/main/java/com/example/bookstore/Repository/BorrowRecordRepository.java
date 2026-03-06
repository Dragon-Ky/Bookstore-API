package com.example.bookstore.Repository;

import com.example.bookstore.Entity.BorrowRecord;
import com.example.bookstore.Entity.ENUM.BorrowStatus;
import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord,Long> {
    List<BorrowRecord> findByUserEmailAndStatus(String email,BorrowStatus status);

    long countByUserEmailAndStatus(String email , BorrowStatus status);

    default BorrowRecord findByIdOrThrow(Long recordId){
        return findById(recordId)
                .orElseThrow(()->new AppException(ErrorCode.RECORDID_NOT_FOUND));
    }

    List<BorrowRecord> findAllByUserEmailOrderByBorrowDateDesc(String email);

    List<BorrowRecord> findAllByOrderByBorrowDateDesc();
    // Kiểm tra xem đã tồn tại bản ghi mượn cuốn sách này mà chưa trả hay chưa
    boolean existsByUserEmailAndBookIdAndStatus(String email, Long bookId, BorrowStatus status);


}
