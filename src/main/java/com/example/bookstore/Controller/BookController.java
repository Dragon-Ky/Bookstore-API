package com.example.bookstore.Controller;

import com.example.bookstore.DTO.ApiResponse;
import com.example.bookstore.DTO.Request.BookRequest;
import com.example.bookstore.DTO.Response.BookResponse;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Service.Book.BookService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Builder
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BookController {
    BookService bookService;
    @GetMapping("/{id}")
    public ApiResponse<Object> getBookById(@PathVariable("id") Long bookId){
        return ApiResponse.builder()
                .code(1000)
                .message("Tìm thấy sách")
                .result(bookService.getBookById(bookId))
                .build();
    }
    @PostMapping
    public ApiResponse<BookResponse> createBook(@RequestBody @Valid BookRequest request){

        return ApiResponse.<BookResponse>builder()
                .code(1000)
                .message("Thêm sách thành công!")
                .result(bookService.creatBook(request))
                .build();

    }
    @PutMapping("/{bookId}")
    public ApiResponse<BookResponse> updateBook(
            @PathVariable Long bookId,
            @RequestBody @Valid BookRequest request
    ){
        return ApiResponse.<BookResponse>builder()
                .code(1000)
                .message("Cập nhật thông tin sách thành công!")
                .result(bookService.updateBook(bookId,request))
                .build();
    }
    @DeleteMapping("/{bookId}")
    public ApiResponse<String> deleteBook(@PathVariable Long bookId){
        bookService.deleteBook(bookId);

        return ApiResponse.<String>builder()
                .code(1000)
                .message("Xóa sách thành công (Soft Delete)!")
                .result("Sách có ID " + bookId + " đã được đưa vào thùng rác.")
                .build();
    }
    @GetMapping
    public ApiResponse<Page<BookResponse>> getAllBooks(
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "size",defaultValue = "10") int size
    ){
        return ApiResponse.<Page<BookResponse>>builder()
                .code(1000)
                .message("Lấy danh sách thành công")
                .result(bookService.getAllBooks(page,size))
                .build();
    }
//    @GetMapping
//    public ApiResponse<Page<BookResponse>> getBooks(
//            @RequestParam(value = "page", defaultValue = "1") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size
//    ) {
//        return ApiResponse.<Page<BookResponse>>builder()
//                .code(1000)
//                .result(bookService.getAllBooksPaging(page, size))
//                .build();
//    }
}
