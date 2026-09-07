package com.ayush.readinghabit.repository;

import com.ayush.readinghabit.entity.ReadingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
            LocalDate readingDate
    );

    long countByUserId(Long userId);

    @Query("""
            SELECT COALESCE(SUM(r.pagesRead), 0)
            FROM ReadingSession r
            WHERE r.user.id = :userId
            """)
    long sumPagesReadByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT COALESCE(SUM(r.durationMinutes), 0)
            FROM ReadingSession r
            WHERE r.user.id = :userId
            """)
    long sumDurationMinutesByUserId(@Param("userId") Long userId);
}