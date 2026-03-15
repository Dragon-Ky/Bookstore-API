package com.example.bookstore.Repository;

import com.example.bookstore.Entity.Book;

import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book,Long>{

    default Book findByIdOrThrow(Long bookId){
        return findById(bookId)
                .orElseThrow(()->new AppException(ErrorCode.BOOK_ID_REQUIRED));
    }
    //SELECT * FROM book WHERE LOWER(title) LIKE LOWER('%keyword%')
    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
}
