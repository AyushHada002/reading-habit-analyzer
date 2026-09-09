package com.ayush.readinghabit.dto;

import java.time.YearMonth;

public class ReadingGoalResponseDTO {

    private Long id;
    private YearMonth month;
    private Integer targetPages;
    private Integer targetMinutes;
    private Long userId;

    public ReadingGoalResponseDTO() {
    }

    public ReadingGoalResponseDTO(
            Long id,
            YearMonth month,
            Integer targetPages,
            Integer targetMinutes,
            Long userId) {

        this.id = id;
        this.month = month;
        this.targetPages = targetPages;
        this.targetMinutes = targetMinutes;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public YearMonth getMonth() {
        return month;
    }

    public Integer getTargetPages() {
        return targetPages;
    }

    public Integer getTargetMinutes() {
        return targetMinutes;
    }

    public Long getUserId() {
        return userId;
    }
}
