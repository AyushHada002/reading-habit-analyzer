package com.ayush.readinghabit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.YearMonth;

public class ReadingGoalRequestDTO {

    @NotNull(message = "Month is required")
    private YearMonth month;

    @NotNull(message = "Target pages is required")
    @Positive(message = "Target pages must be greater than 0")
    private Integer targetPages;

    @NotNull(message = "Target minutes is required")
    @Positive(message = "Target minutes must be greater than 0")
    private Integer targetMinutes;

    @NotNull(message = "User ID is required")
    private Long userId;

    public ReadingGoalRequestDTO() {
    }

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }

    public Integer getTargetPages() {
        return targetPages;
    }

    public void setTargetPages(Integer targetPages) {
        this.targetPages = targetPages;
    }

    public Integer getTargetMinutes() {
        return targetMinutes;
    }

    public void setTargetMinutes(Integer targetMinutes) {
        this.targetMinutes = targetMinutes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
