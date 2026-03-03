package com.example.bookstore.Service.Book;

import com.example.bookstore.DTO.Request.BookRequest;
import com.example.bookstore.DTO.Response.BookResponse;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import com.example.bookstore.Mapper.BookMapper;
import com.example.bookstore.Repository.BookRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // Tự tạo constructor cho các field 'final'
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {
    final BookRepository bookRepository;

    final BookMapper bookMapper;

    @Transactional
    public BookResponse creatBook(BookRequest request){
        // 1. Chuyển từ DTO sang Entity nhờ MapStruct
        Book book = bookMapper.toBook(request);
        // 2. Thiết lập số lượng sách sẵn có ban đầu bằng tổng số lượng nhập vào
        book.setAvailableQuantity(request.getTotalQuantity());
        // 3. Lưu vào Database
        book=bookRepository.save(book);
        // 4. Trả về BookResponse (đã được map ngược lại từ Entity)
        return bookMapper.toBookResponse(book);

    }
    private Book checkExitId(Long bookId){
        return  bookRepository.findById(bookId)
                .orElseThrow(()->new AppException(ErrorCode.BOOK_NOT_FOUND));
    }
    @Transactional
    public BookResponse updateBook(Long bookId,BookRequest request){
        //1. tìm sách trong data bằng id
        Book book = checkExitId(bookId);
        //2. cập nhật dữ liệu từ request vào đối tượng book tìm thấy
        bookMapper.updateBook(book,request);
        //3. cập nhật số lượng sẵn có
        book.setAvailableQuantity(request.getTotalQuantity());
        //4.Lưu vào data
        book =bookRepository.save(book);
        //5. trả về kết quả
        return bookMapper.toBookResponse(book);
    }
    @Transactional
    public void deleteBook(Long bookId){
        //1.kiểm tra có tồn tại ko
        Book book = checkExitId(bookId);
        //2. set delete true
        book.setDeleted(true);
        //3.lưu
        bookRepository.save(book);
    }
    public BookResponse getBookDetail(Long bookId){
        Book book = checkExitId(bookId);

        return bookMapper.toBookResponse(book);
    }

    public Page<BookResponse> getAllBooks(int page,int size){
        // Tạo đối tượng Pageable (trang bắt đầu từ 0)
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        // Gọi repository lấy dữ liệu phân trang
        Page<Book> bookPage = bookRepository.findAll(pageable);
        // Chuyển đổi từ Page<Book> (Entity) sang Page<BookResponse> (DTO)
        return bookPage.map(bookMapper::toBookResponse);
    }
    public Page<BookResponse> getAllBooksPaging(int page,int size){
        Pageable pageable = PageRequest.of(page - 1, size); // page - 1 vì Spring tính từ 0
        return bookRepository.findAll(pageable).map(bookMapper::toBookResponse);
    }
    public Book getBookById(Long bookId){
        return bookRepository.findByIdOrThrow(bookId);
    }
}
