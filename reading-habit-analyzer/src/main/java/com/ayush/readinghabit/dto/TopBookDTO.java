package com.ayush.readinghabit.dto;

public class TopBookDTO {

    private Long bookId;
    private String title;
    private String author;
    private long pagesRead;
    private long readingMinutes;
    private long sessionCount;

    public TopBookDTO() {
    }

    public TopBookDTO(
            Long bookId,
            String title,
            String author,
            long pagesRead,
            long readingMinutes,
            long sessionCount) {

        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.pagesRead = pagesRead;
        this.readingMinutes = readingMinutes;
        this.sessionCount = sessionCount;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public long getPagesRead() {
        return pagesRead;
    }

    public long getReadingMinutes() {
        return readingMinutes;
    }

    public long getSessionCount() {
        return sessionCount;
    }
}
