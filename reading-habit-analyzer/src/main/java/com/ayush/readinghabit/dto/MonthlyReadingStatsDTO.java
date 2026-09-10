package com.ayush.readinghabit.dto;

import java.time.YearMonth;

public class MonthlyReadingStatsDTO {

    private Long userId;
    private YearMonth month;

    private long totalSessions;
    private long totalPagesRead;
    private long totalReadingMinutes;

    private double averagePagesPerSession;
    private double averageMinutesPerSession;

    public MonthlyReadingStatsDTO() {
    }

    public MonthlyReadingStatsDTO(
            Long userId,
            YearMonth month,
            long totalSessions,
            long totalPagesRead,
            long totalReadingMinutes,
            double averagePagesPerSession,
            double averageMinutesPerSession) {

        this.userId = userId;
        this.month = month;
        this.totalSessions = totalSessions;
        this.totalPagesRead = totalPagesRead;
        this.totalReadingMinutes = totalReadingMinutes;
        this.averagePagesPerSession = averagePagesPerSession;
        this.averageMinutesPerSession = averageMinutesPerSession;
    }

    public Long getUserId() {
        return userId;
    }

    public YearMonth getMonth() {
        return month;
    }

    public long getTotalSessions() {
        return totalSessions;
    }

    public long getTotalPagesRead() {
        return totalPagesRead;
    }

    public long getTotalReadingMinutes() {
        return totalReadingMinutes;
    }

    public double getAveragePagesPerSession() {
        return averagePagesPerSession;
    }

    public double getAverageMinutesPerSession() {
        return averageMinutesPerSession;
    }
}