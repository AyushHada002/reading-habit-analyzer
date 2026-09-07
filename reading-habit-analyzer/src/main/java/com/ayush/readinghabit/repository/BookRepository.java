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
}