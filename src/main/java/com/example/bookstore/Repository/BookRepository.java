package com.example.bookstore.Repository;

import com.example.bookstore.Entity.Book;

import com.example.bookstore.Exception.AppException;
import com.example.bookstore.Exception.ErrorCode;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book,Long>{

    default Book findByIdOrThrow(Long bookId){
        return findById(bookId)
                .orElseThrow(()->new AppException(ErrorCode.BOOK_ID_REQUIRED));
    }
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchByKeyword(@Param("keyword") String keyword);

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);


    @Query(value = "SELECT * FROM books ORDER BY RANDOM() LIMIT 5", nativeQuery = true)
    List<Book> getRandomBooks();

    // Cập nhật câu SQL: Thêm logic tìm theo category và description (dùng biến keyword)
    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', CAST(:title as String), '%'))) AND " +
            "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', CAST(:author as String), '%'))) AND " +
            "(:categories IS NULL OR LOWER(b.categories) LIKE LOWER(CONCAT('%', CAST(:categories as String), '%'))) AND " +
            "(:keyword IS NULL OR LOWER(b.description) LIKE LOWER(CONCAT('%', CAST(:keyword as String), '%')))")
    List<Book> searchDynamicByAi(
            @Param("title") String title,
            @Param("author") String author,
            @Param("categories") String categories, // Tham số mới
            @Param("keyword") String keyword,   // Tham số mới
            Sort sort
    );
}
