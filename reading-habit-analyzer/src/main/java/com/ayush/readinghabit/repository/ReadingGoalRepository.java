package com.ayush.readinghabit.repository;

import com.ayush.readinghabit.entity.ReadingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface ReadingGoalRepository
        extends JpaRepository<ReadingGoal, Long> {

    Optional<ReadingGoal> findByUserIdAndMonth(
            Long userId,
            YearMonth month
    );

    List<ReadingGoal> findByUserIdOrderByMonthDesc(
            Long userId
    );

    Page<ReadingGoal> findByUserId(
            Long userId,
            Pageable pageable
    );

    boolean existsByUserIdAndMonth(
            Long userId,
            YearMonth month
    );
}
