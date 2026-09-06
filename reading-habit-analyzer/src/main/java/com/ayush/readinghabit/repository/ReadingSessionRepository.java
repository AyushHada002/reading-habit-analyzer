package com.ayush.readinghabit.repository;

import com.ayush.readinghabit.entity.ReadingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadingSessionRepository
        extends JpaRepository<ReadingSession, Long> {

    List<ReadingSession> findByUserId(Long userId);

    List<ReadingSession> findByBookId(Long bookId);

    List<ReadingSession> findByUserIdAndBookId(
            Long userId,
            Long bookId
    );

    List<ReadingSession> findByReadingDate(
            java.time.LocalDate readingDate
    );
}
