package com.ayush.readinghabit.controller;

import com.ayush.readinghabit.dto.ReadingAnalyticsDTO;
import com.ayush.readinghabit.service.ReadingAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ayush.readinghabit.dto.DailyReadingStatsDTO;
import com.ayush.readinghabit.dto.MonthlyReadingStatsDTO;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class ReadingAnalyticsController {

    private final ReadingAnalyticsService readingAnalyticsService;

    public ReadingAnalyticsController(
            ReadingAnalyticsService readingAnalyticsService) {

        this.readingAnalyticsService = readingAnalyticsService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ReadingAnalyticsDTO> getUserAnalytics(
            @PathVariable Long userId) {

        ReadingAnalyticsDTO analytics =
                readingAnalyticsService.getUserAnalytics(userId);

        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/user/{userId}/monthly")
    public ResponseEntity<MonthlyReadingStatsDTO> getMonthlyStats(
            @PathVariable Long userId,
            @RequestParam YearMonth month) {

        MonthlyReadingStatsDTO stats =
                readingAnalyticsService.getMonthlyStats(
                        userId,
                        month
                );

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/user/{userId}/daily")
    public ResponseEntity<List<DailyReadingStatsDTO>> getDailyStats(
            @PathVariable Long userId,
            @RequestParam YearMonth month) {

        List<DailyReadingStatsDTO> stats =
                readingAnalyticsService.getDailyStats(
                        userId,
                        month
                );

        return ResponseEntity.ok(stats);
    }


}
