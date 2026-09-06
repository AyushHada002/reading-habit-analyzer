package com.ayush.readinghabit.dto;

import java.time.LocalDate;

public class ReadingSessionResponseDTO {

    private Long id;
    private LocalDate readingDate;
    private Integer durationMinutes;
    private Integer pagesRead;
    private String notes;
    private Long userId;
    private Long bookId;
    private String bookTitle;

    public ReadingSessionResponseDTO() {
    }

    public ReadingSessionResponseDTO(
            Long id,
            LocalDate readingDate,
            Integer durationMinutes,
            Integer pagesRead,
            String notes,
            Long userId,
            Long bookId,
            String bookTitle) {

        this.id = id;
        this.readingDate = readingDate;
        this.durationMinutes = durationMinutes;
        this.pagesRead = pagesRead;
        this.notes = notes;
        this.userId = userId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getReadingDate() {
        return readingDate;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public Integer getPagesRead() {
        return pagesRead;
    }

    public String getNotes() {
        return notes;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
