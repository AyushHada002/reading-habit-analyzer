package com.ayush.readinghabit.repository;

import com.ayush.readinghabit.entity.Book;
import com.ayush.readinghabit.entity.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByUserId(Long userId);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(
            Long userId,
            BookStatus status
    );

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByGenreIgnoreCase(String genre);

    List<Book> findByStatus(BookStatus status);

    List<Book> findByUserIdAndTitleContainingIgnoreCase(
            Long userId,
            String title
    );

    List<Book> findByUserIdAndAuthorContainingIgnoreCase(
            Long userId,
            String author
    );

    List<Book> findByUserIdAndGenreIgnoreCase(
            Long userId,
            String genre
    );

    List<Book> findByUserIdAndStatus(
            Long userId,
            BookStatus status
    );
}