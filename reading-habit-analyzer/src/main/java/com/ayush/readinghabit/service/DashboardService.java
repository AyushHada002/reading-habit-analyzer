package com.ayush.readinghabit.service;

import com.ayush.readinghabit.dto.DashboardDTO;
import com.ayush.readinghabit.dto.ReadingGoalProgressDTO;
import com.ayush.readinghabit.entity.BookStatus;
import com.ayush.readinghabit.entity.User;
import com.ayush.readinghabit.exception.ResourceNotFoundException;
import com.ayush.readinghabit.repository.BookRepository;
import com.ayush.readinghabit.repository.ReadingGoalRepository;
import com.ayush.readinghabit.repository.ReadingSessionRepository;
import com.ayush.readinghabit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReadingSessionRepository readingSessionRepository;
    private final ReadingGoalRepository readingGoalRepository;
    private final ReadingAnalyticsService readingAnalyticsService;
    private final ReadingGoalService readingGoalService;

    public DashboardService(
            UserRepository userRepository,
            BookRepository bookRepository,
            ReadingSessionRepository readingSessionRepository,
            ReadingGoalRepository readingGoalRepository,
            ReadingAnalyticsService readingAnalyticsService,
            ReadingGoalService readingGoalService) {

        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.readingSessionRepository = readingSessionRepository;
        this.readingGoalRepository = readingGoalRepository;
        this.readingAnalyticsService = readingAnalyticsService;
        this.readingGoalService = readingGoalService;
    }

    public DashboardDTO getDashboard(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        long totalBooks =
                bookRepository.countByUserId(user.getId());

        long completedBooks =
                bookRepository.countByUserIdAndStatus(
                        user.getId(),
                        BookStatus.COMPLETED
                );

        long currentlyReading =
                bookRepository.countByUserIdAndStatus(
                        user.getId(),
                        BookStatus.READING
                );

        long booksToRead =
                bookRepository.countByUserIdAndStatus(
                        user.getId(),
                        BookStatus.TO_READ
                );

        long totalPagesRead =
                readingSessionRepository
                        .sumPagesReadByUserId(user.getId());

        long totalReadingMinutes =
                readingSessionRepository
                        .sumDurationMinutesByUserId(user.getId());

        long totalSessions =
                readingSessionRepository
                        .countByUserId(user.getId());

        double averageSessionDuration =
                totalSessions == 0
                        ? 0.0
                        : round(
                        (double) totalReadingMinutes
                                / totalSessions
                );

        double averagePagesPerSession =
                totalSessions == 0
                        ? 0.0
                        : round(
                        (double) totalPagesRead
                                / totalSessions
                );

        ReadingAnalyticsService.AnalyticsData analytics =
                readingAnalyticsService
                        .getAnalyticsData(userId);

        YearMonth currentMonth =
                YearMonth.now();

        ReadingGoalProgressDTO currentMonthGoal =
                readingGoalRepository
                        .findByUserIdAndMonth(
                                userId,
                                currentMonth
                        )
                        .map(goal ->
                                readingGoalService
                                        .getGoalProgress(goal.getId())
                        )
                        .orElse(null);

        return new DashboardDTO(
                userId,
                totalBooks,
                completedBooks,
                currentlyReading,
                booksToRead,
                totalPagesRead,
                totalReadingMinutes,
                averageSessionDuration,
                averagePagesPerSession,
                analytics.currentReadingStreak(),
                currentMonth,
                currentMonthGoal
        );
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
