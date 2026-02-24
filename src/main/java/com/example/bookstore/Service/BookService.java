package com.example.bookstore.Service;

import com.example.bookstore.DTO.Request.BookRequest;
import com.example.bookstore.DTO.Response.BookResponse;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Mapper.BookMapper;
import com.example.bookstore.Repository.BookRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Tự tạo constructor cho các field 'final'
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {
    final BookRepository bookRepository;

    final BookMapper bookMapper;

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
    public BookResponse getBookDetail(Long id){
        Book book = bookRepository.findById(id).orElseThrow();

        return bookMapper.toBookResponse(book);
    }
}
