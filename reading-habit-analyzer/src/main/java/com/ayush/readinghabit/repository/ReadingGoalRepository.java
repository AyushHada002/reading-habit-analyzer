package com.ayush.readinghabit.repository;

import com.ayush.readinghabit.entity.ReadingGoal;
import org.springframework.data.jpa.repository.JpaRepository;

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

    boolean existsByUserIdAndMonth(
            Long userId,
            YearMonth month
    );
}
