package com.ayush.readinghabit.dto;

public class ReadingAnalyticsDTO {

    private Long userId;

    private long totalBooks;

    private long completedBooks;

    private long currentlyReading;

    private long booksToRead;

    private long totalPagesRead;

    private long totalReadingMinutes;

    private double averageSessionDuration;

    private double averagePagesPerSession;

    private int currentReadingStreak;

    public ReadingAnalyticsDTO() {
    }

    public ReadingAnalyticsDTO(
            Long userId,
            long totalBooks,
            long completedBooks,
            long currentlyReading,
            long booksToRead,
            long totalPagesRead,
            long totalReadingMinutes,
            double averageSessionDuration,
            double averagePagesPerSession,
            int currentReadingStreak) {

        this.userId = userId;
        this.totalBooks = totalBooks;
        this.completedBooks = completedBooks;
        this.currentlyReading = currentlyReading;
        this.booksToRead = booksToRead;
        this.totalPagesRead = totalPagesRead;
        this.totalReadingMinutes = totalReadingMinutes;
        this.averageSessionDuration = averageSessionDuration;
        this.averagePagesPerSession = averagePagesPerSession;
        this.currentReadingStreak = currentReadingStreak;
    }

    public Long getUserId() {
        return userId;
    }

    public long getTotalBooks() {
        return totalBooks;
    }

    public long getCompletedBooks() {
        return completedBooks;
    }

    public long getCurrentlyReading() {
        return currentlyReading;
    }

    public long getBooksToRead() {
        return booksToRead;
    }

    public long getTotalPagesRead() {
        return totalPagesRead;
    }

    public long getTotalReadingMinutes() {
        return totalReadingMinutes;
    }

    public double getAverageSessionDuration() {
        return averageSessionDuration;
    }

    public double getAveragePagesPerSession() {
        return averagePagesPerSession;
    }

    public int getCurrentReadingStreak() {
        return currentReadingStreak;
    }
}
