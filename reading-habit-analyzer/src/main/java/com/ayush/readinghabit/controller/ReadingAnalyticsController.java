package com.ayush.readinghabit.controller;

import com.ayush.readinghabit.dto.DailyReadingStatsDTO;
import com.ayush.readinghabit.dto.MonthlyReadingStatsDTO;
import com.ayush.readinghabit.dto.ReadingAnalyticsDTO;
import com.ayush.readinghabit.service.AuthenticatedUserService;
import com.ayush.readinghabit.service.ReadingAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class ReadingAnalyticsController {

    private final ReadingAnalyticsService analyticsService;
    private final AuthenticatedUserService authenticatedUserService;

    public ReadingAnalyticsController(
            ReadingAnalyticsService analyticsService,
            AuthenticatedUserService authenticatedUserService) {

        this.analyticsService = analyticsService;
        this.authenticatedUserService =
                authenticatedUserService;
    }

    @GetMapping("/my")
    public ResponseEntity<ReadingAnalyticsDTO> getMyAnalytics() {

        Long currentUserId =
                authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                analyticsService.getUserAnalytics(
                        currentUserId
                )
        );
    }

    @GetMapping("/my/monthly")
    public ResponseEntity<MonthlyReadingStatsDTO> getMyMonthlyStats(
            @RequestParam YearMonth month) {

        Long currentUserId =
                authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                analyticsService.getMonthlyStats(
                        currentUserId,
                        month
                )
        );
    }

    @GetMapping("/my/daily")
    public ResponseEntity<List<DailyReadingStatsDTO>> getMyDailyStats(
            @RequestParam YearMonth month) {

        Long currentUserId =
                authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                analyticsService.getDailyStats(
                        currentUserId,
                        month
                )
        );
    }
}
