package com.ayush.readinghabit.service;

import com.ayush.readinghabit.dto.ReadingAnalyticsDTO;
import com.ayush.readinghabit.entity.BookStatus;
import com.ayush.readinghabit.entity.ReadingSession;
import com.ayush.readinghabit.exception.ResourceNotFoundException;
import com.ayush.readinghabit.repository.BookRepository;
import com.ayush.readinghabit.repository.ReadingSessionRepository;
import com.ayush.readinghabit.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.ayush.readinghabit.dto.DailyReadingStatsDTO;
import com.ayush.readinghabit.dto.MonthlyReadingStatsDTO;

import java.time.YearMonth;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class ReadingAnalyticsService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReadingSessionRepository readingSessionRepository;

    public ReadingAnalyticsService(
            UserRepository userRepository,
            BookRepository bookRepository,
            ReadingSessionRepository readingSessionRepository) {

        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.readingSessionRepository = readingSessionRepository;
    }

    public ReadingAnalyticsDTO getUserAnalytics(Long userId) {

        // 1. Check whether user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        // 2. Book statistics
        long totalBooks =
                bookRepository.countByUserId(userId);

        long completedBooks =
                bookRepository.countByUserIdAndStatus(
                        userId,
                        BookStatus.COMPLETED
                );

        long currentlyReading =
                bookRepository.countByUserIdAndStatus(
                        userId,
                        BookStatus.READING
                );

        long booksToRead =
                bookRepository.countByUserIdAndStatus(
                        userId,
                        BookStatus.TO_READ
                );

        // 3. Reading session statistics
        long totalPagesRead =
                readingSessionRepository
                        .sumPagesReadByUserId(userId);

        long totalReadingMinutes =
                readingSessionRepository
                        .sumDurationMinutesByUserId(userId);

        long totalSessions =
                readingSessionRepository
                        .countByUserId(userId);

        // 4. Average calculations
        double averageSessionDuration = 0.0;
        double averagePagesPerSession = 0.0;

        if (totalSessions > 0) {

            averageSessionDuration =
                    (double) totalReadingMinutes / totalSessions;

            averagePagesPerSession =
                    (double) totalPagesRead / totalSessions;
        }

        // 5. Calculate reading streak
        int currentReadingStreak =
                calculateCurrentReadingStreak(userId);

        // 6. Return analytics DTO
        return new ReadingAnalyticsDTO(
                userId,
                totalBooks,
                completedBooks,
                currentlyReading,
                booksToRead,
                totalPagesRead,
                totalReadingMinutes,
                roundToTwoDecimals(averageSessionDuration),
                roundToTwoDecimals(averagePagesPerSession),
                currentReadingStreak
        );
    }

    private int calculateCurrentReadingStreak(Long userId) {

        List<ReadingSession> sessions =
                readingSessionRepository.findByUserId(userId);

        if (sessions.isEmpty()) {
            return 0;
        }

        List<LocalDate> readingDates = sessions.stream()
                .map(ReadingSession::getReadingDate)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        LocalDate today = LocalDate.now();

        LocalDate latestReadingDate =
                readingDates.get(0);

        /*
         * If the user has not read today or yesterday,
         * there is no active current streak.
         */
        if (latestReadingDate.isBefore(today.minusDays(1))) {
            return 0;
        }

        int streak = 0;

        LocalDate expectedDate = latestReadingDate;

        for (LocalDate readingDate : readingDates) {

            if (readingDate.equals(expectedDate)) {

                streak++;

                expectedDate =
                        expectedDate.minusDays(1);

            } else if (readingDate.isBefore(expectedDate)) {

                break;
            }
        }

        return streak;
    }

    private double roundToTwoDecimals(double value) {

        return Math.round(value * 100.0) / 100.0;
    }

    public MonthlyReadingStatsDTO getMonthlyStats(
            Long userId,
            YearMonth month) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        LocalDate startDate =
                month.atDay(1);

        LocalDate endDate =
                month.atEndOfMonth();

        long totalSessions =
                readingSessionRepository
                        .countByUserIdAndDateRange(
                                userId,
                                startDate,
                                endDate
                        );

        long totalPagesRead =
                readingSessionRepository
                        .sumPagesReadByUserIdAndDateRange(
                                userId,
                                startDate,
                                endDate
                        );

        long totalReadingMinutes =
                readingSessionRepository
                        .sumDurationMinutesByUserIdAndDateRange(
                                userId,
                                startDate,
                                endDate
                        );

        double averagePagesPerSession = 0.0;
        double averageMinutesPerSession = 0.0;

        if (totalSessions > 0) {

            averagePagesPerSession =
                    (double) totalPagesRead
                            / totalSessions;

            averageMinutesPerSession =
                    (double) totalReadingMinutes
                            / totalSessions;
        }

        return new MonthlyReadingStatsDTO(
                userId,
                month,
                totalSessions,
                totalPagesRead,
                totalReadingMinutes,
                roundToTwoDecimals(
                        averagePagesPerSession
                ),
                roundToTwoDecimals(
                        averageMinutesPerSession
                )
        );
    }

    public List<DailyReadingStatsDTO> getDailyStats(
            Long userId,
            YearMonth month) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        LocalDate startDate =
                month.atDay(1);

        LocalDate endDate =
                month.atEndOfMonth();

        List<Object[]> results =
                readingSessionRepository
                        .findDailyReadingStats(
                                userId,
                                startDate,
                                endDate
                        );

        List<DailyReadingStatsDTO> dailyStats =
                new ArrayList<>();

        for (Object[] row : results) {

            LocalDate date =
                    (LocalDate) row[0];

            long totalSessions =
                    ((Number) row[1]).longValue();

            long pagesRead =
                    ((Number) row[2]).longValue();

            long readingMinutes =
                    ((Number) row[3]).longValue();

            dailyStats.add(
                    new DailyReadingStatsDTO(
                            date,
                            totalSessions,
                            pagesRead,
                            readingMinutes
                    )
            );
        }

        return dailyStats;
    }
}
