package com.ayush.readinghabit.dto;

import com.ayush.readinghabit.entity.BookStatus;

public class BookProgressDTO {

    private Long bookId;
    private String title;
    private Integer totalPages;
    private long pagesRead;
    private long remainingPages;
    private double progressPercentage;
    private BookStatus status;

    public BookProgressDTO() {
    }

    public BookProgressDTO(
            Long bookId,
            String title,
            Integer totalPages,
            long pagesRead,
            long remainingPages,
            double progressPercentage,
            BookStatus status) {

        this.bookId = bookId;
        this.title = title;
        this.totalPages = totalPages;
        this.pagesRead = pagesRead;
        this.remainingPages = remainingPages;
        this.progressPercentage = progressPercentage;
        this.status = status;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public long getPagesRead() {
        return pagesRead;
    }

    public long getRemainingPages() {
        return remainingPages;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public BookStatus getStatus() {
        return status;
    }
}
