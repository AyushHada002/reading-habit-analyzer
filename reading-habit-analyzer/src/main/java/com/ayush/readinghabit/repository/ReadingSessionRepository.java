package com.ayush.readinghabit.repository;

import com.ayush.readinghabit.entity.ReadingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDate;
import java.time.YearMonth;

public interface ReadingSessionRepository
        extends JpaRepository<ReadingSession, Long>,
        JpaSpecificationExecutor<ReadingSession> {

    List<ReadingSession> findByUserId(Long userId);

    List<ReadingSession> findByBookId(Long bookId);

    List<ReadingSession> findByUserIdAndBookId(
            Long userId,
            Long bookId
    );

    List<ReadingSession> findByReadingDate(
            LocalDate readingDate
    );

    Page<ReadingSession> findByUserId(
            Long userId,
            Pageable pageable
    );

    Page<ReadingSession> findByBookId(
            Long bookId,
            Pageable pageable
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

    @Query("""
        SELECT COALESCE(SUM(r.pagesRead), 0)
        FROM ReadingSession r
        WHERE r.book.id = :bookId
        """)
    long sumPagesReadByBookId(@Param("bookId") Long bookId);

    @Query("""
        SELECT COALESCE(SUM(r.pagesRead), 0)
        FROM ReadingSession r
        WHERE r.user.id = :userId
        AND r.readingDate >= :startDate
        AND r.readingDate <= :endDate
        """)
    long sumPagesReadByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT COALESCE(SUM(r.durationMinutes), 0)
        FROM ReadingSession r
        WHERE r.user.id = :userId
        AND r.readingDate >= :startDate
        AND r.readingDate <= :endDate
        """)
    long sumDurationMinutesByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT COUNT(r)
        FROM ReadingSession r
        WHERE r.user.id = :userId
        AND r.readingDate >= :startDate
        AND r.readingDate <= :endDate
        """)
    long countByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT r.readingDate,
               COUNT(r),
               COALESCE(SUM(r.pagesRead), 0),
               COALESCE(SUM(r.durationMinutes), 0)
        FROM ReadingSession r
        WHERE r.user.id = :userId
        AND r.readingDate >= :startDate
        AND r.readingDate <= :endDate
        GROUP BY r.readingDate
        ORDER BY r.readingDate
        """)
    List<Object[]> findDailyReadingStats(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}