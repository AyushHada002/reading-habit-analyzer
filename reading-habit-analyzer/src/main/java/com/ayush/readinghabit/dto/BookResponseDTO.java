package com.ayush.readinghabit.dto;

import com.ayush.readinghabit.entity.BookStatus;

import java.time.LocalDate;

public class BookResponseDTO {

    private Long id;
    private String title;
    private String author;
    private String genre;
    private Integer totalPages;
    private BookStatus status;
    private LocalDate startedDate;
    private LocalDate completedDate;
    private Long userId;

    public BookResponseDTO() {
    }

    public BookResponseDTO(
            Long id,
            String title,
            String author,
            String genre,
            Integer totalPages,
            BookStatus status,
            LocalDate startedDate,
            LocalDate completedDate,
            Long userId) {

        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.totalPages = totalPages;
        this.status = status;
        this.startedDate = startedDate;
        this.completedDate = completedDate;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public BookStatus getStatus() {
        return status;
    }

    public LocalDate getStartedDate() {
        return startedDate;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public Long getUserId() {
        return userId;
    }
}
