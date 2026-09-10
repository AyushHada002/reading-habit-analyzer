package com.ayush.readinghabit.dto;

import com.ayush.readinghabit.entity.BookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class BookRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotNull(message = "Total pages is required")
    @Positive(message = "Total pages must be greater than 0")
    private Integer totalPages;

    private BookStatus status;

    private LocalDate startedDate;

    private LocalDate completedDate;

    @NotNull(message = "User ID is required")
    private Long userId;

    public BookRequestDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public LocalDate getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(LocalDate startedDate) {
        this.startedDate = startedDate;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}