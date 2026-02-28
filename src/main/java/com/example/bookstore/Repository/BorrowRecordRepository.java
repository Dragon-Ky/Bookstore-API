package com.example.bookstore.Repository;

import com.example.bookstore.Entity.BorrowRecord;
import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord,Long> {
    List<BorrowRecord> findByUserEmailAndIsReturnedFalse(String email);

    default BorrowRecord findByIdOrThrow(Long recordId){
        return findById(recordId)
                .orElseThrow(()->new AppException(ErrorCode.RECORDID_NOT_FOUND));
    }
}
