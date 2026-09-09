package com.ayush.readinghabit.dto;

import java.time.YearMonth;

public class ReadingGoalProgressDTO {

    private Long goalId;
    private Long userId;
    private YearMonth month;

    private int targetPages;
    private long pagesRead;
    private double pageProgressPercentage;

    private int targetMinutes;
    private long minutesRead;
    private double timeProgressPercentage;

    private double overallProgressPercentage;

    public ReadingGoalProgressDTO() {
    }

    public ReadingGoalProgressDTO(
            Long goalId,
            Long userId,
            YearMonth month,
            int targetPages,
            long pagesRead,
            double pageProgressPercentage,
            int targetMinutes,
            long minutesRead,
            double timeProgressPercentage,
            double overallProgressPercentage) {

        this.goalId = goalId;
        this.userId = userId;
        this.month = month;
        this.targetPages = targetPages;
        this.pagesRead = pagesRead;
        this.pageProgressPercentage = pageProgressPercentage;
        this.targetMinutes = targetMinutes;
        this.minutesRead = minutesRead;
        this.timeProgressPercentage = timeProgressPercentage;
        this.overallProgressPercentage = overallProgressPercentage;
    }

    public Long getGoalId() {
        return goalId;
    }

    public Long getUserId() {
        return userId;
    }

    public YearMonth getMonth() {
        return month;
    }

    public int getTargetPages() {
        return targetPages;
    }

    public long getPagesRead() {
        return pagesRead;
    }

    public double getPageProgressPercentage() {
        return pageProgressPercentage;
    }

    public int getTargetMinutes() {
        return targetMinutes;
    }

    public long getMinutesRead() {
        return minutesRead;
    }

    public double getTimeProgressPercentage() {
        return timeProgressPercentage;
    }

    public double getOverallProgressPercentage() {
        return overallProgressPercentage;
    }
}
