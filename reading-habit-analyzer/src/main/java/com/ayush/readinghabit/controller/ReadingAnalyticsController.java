package com.ayush.readinghabit.controller;

import com.ayush.readinghabit.dto.ReadingAnalyticsDTO;
import com.ayush.readinghabit.service.ReadingAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
