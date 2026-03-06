package com.example.bookstore.Service;

import com.example.bookstore.DTO.Request.BorrowRequest;
import com.example.bookstore.DTO.Response.BorrowResponse;
import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Entity.BorrowRecord;
import com.example.bookstore.Entity.ENUM.BorrowStatus;
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

import java.time.LocalDateTime;
import java.util.List;

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

        // check trùng sách
        boolean isAlreadyBorrowing = borrowRepository.existsByUserEmailAndBookIdAndStatus(
                email,
                request.getBookId(),
                BorrowStatus.BORROWING
        );

        if (isAlreadyBorrowing) {
            throw new AppException(ErrorCode.BOOK_ALREADY_BORROWED);
        }
        // bổ sung check sách
        long borrowingCount = borrowRepository.countByUserEmailAndStatus(email,BorrowStatus.BORROWING);
        if (borrowingCount>=5){
            throw new AppException(ErrorCode.MAX_BORROW_REACHED);
        }
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
        record.setBorrowDate(LocalDateTime.now()); // Ngày mượn là hiện tại
        record.setDueDate(LocalDateTime.now().plusDays(7)); // Hạn trả là 14 ngày sau
        record.setStatus(BorrowStatus.BORROWING);
        borrowRepository.save(record);

        return borrowMapper.toResponse(record);
    }
    @Transactional
    public BorrowResponse returnBook(Long recordId){
        //1. tìm phiếu mượn sách
        BorrowRecord record = borrowRepository.findByIdOrThrow(recordId);
        //2. kiểm tra lại sách đã chưa ( tránh trả 2 lần )
        if (record.getStatus().equals(BorrowStatus.RETURNED)){
            throw new AppException(ErrorCode.BOOK_ALREADY_RETURNED);
        }
        record.setStatus(BorrowStatus.RETURNED);
        //3. đánh đấu trả lại cùng thời gian
        record.getStatus().equals(BorrowStatus.RETURNED);
        record.setReturnDate(LocalDateTime.now());
        //4. thêm sách vào lại kho
        Book book = record.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity()+1);

        //5. lưu sách và phiếu
        bookRepository.save(book);
        borrowRepository.save(record);

        return borrowMapper.toResponse(record);
    }
    public List<BorrowResponse> getMyHistory(){
        String email =SecurityUtils.getCurrentUserEmail();
        return borrowRepository.findAllByUserEmailOrderByBorrowDateDesc(email)
                .stream()
                .map(borrowMapper::toResponse)
                .toList();
    }
    public List<BorrowResponse> getAllHistory(){
        return borrowRepository.findAllByOrderByBorrowDateDesc()
                .stream()
                .map(borrowMapper::toResponse)
                .toList();
    }

    @Transactional
    public BorrowResponse reportLost(Long recordId){
        // 1. Tìm phiếu mượn sách theo ID, nếu không thấy sẽ ném ngoại lệ
        BorrowRecord record = borrowRepository.findByIdOrThrow(recordId);
        // 2. Kiểm tra nếu sách đã trả hoặc đã báo mất rồi thì không xử lý nữa
        if (record.getStatus().equals(BorrowStatus.RETURNED) ||
            record.getStatus().equals(BorrowStatus.LOST)){
            throw new AppException(ErrorCode.INVALID_STATUS_CHANGE);
        }
        // 3. Cập nhật trạng thái thành LOST (Mất sách)
        record.setStatus(BorrowStatus.LOST);
        // cập nhật thời gian người dùng báo
        record.getStatus().equals(BorrowStatus.RETURNED);
        record.setReturnDate(LocalDateTime.now());
        // 4. Lưu thay đổi trạng thái vào cơ sở dữ liệu
        borrowRepository.save(record);

        return borrowMapper.toResponse(record);
    }
}
