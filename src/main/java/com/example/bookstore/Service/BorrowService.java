package com.example.bookstore.Service;

import com.example.bookstore.DTO.Request.BorrowRequest;
import com.example.bookstore.DTO.Response.BorrowResponse;
import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Entity.BorrowRecord;
import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import com.example.bookstore.Mapper.BorrowMapper;
import com.example.bookstore.Repository.BookRepository;
import com.example.bookstore.Repository.BorrowRecordRepository;
import com.example.bookstore.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BorrowService {
    BorrowRecordRepository borrowRepository;
    BookRepository bookRepository;
    UserRepository userRepository;
    BorrowMapper borrowMapper;


    @Transactional // lỗi thì ko trừ số lượng sách
    public BorrowResponse borrowBook(BorrowRequest request){
        // 1. Lấy email người dùng hiện tại từ SecurityContext
        String email = SecurityUtils.getCurrentUserEmail();
        AppUser user = userRepository.findByEmailOrThrow(email);

        //2. kiểm tra sách
        Book book = bookRepository.findByIdOrThrow(request.getBookId());
        if (book.getAvailableQuantity()<=0){
            throw  new AppException(ErrorCode.BOOK_OUT_OF_STOCK);
        }
        //3 trừ số lượng sách
        book.setAvailableQuantity(book.getAvailableQuantity()-1);
        bookRepository.save(book);

        //4 Tạo phiếu mượn sách
        BorrowRecord record = borrowMapper.toBorrow(user,book);
        borrowRepository.save(record);

        return borrowMapper.toResponse(record);
    }
}
