package com.ayush.readinghabit.dto;

import java.time.LocalDate;

public class DailyReadingStatsDTO {

    private LocalDate date;
    private long totalSessions;
    private long pagesRead;
    private long readingMinutes;

    public DailyReadingStatsDTO() {
    }

    public DailyReadingStatsDTO(
            LocalDate date,
            long totalSessions,
            long pagesRead,
            long readingMinutes) {

        this.date = date;
        this.totalSessions = totalSessions;
        this.pagesRead = pagesRead;
        this.readingMinutes = readingMinutes;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getTotalSessions() {
        return totalSessions;
    }

    public long getPagesRead() {
        return pagesRead;
    }

    public long getReadingMinutes() {
        return readingMinutes;
    }
}
