package com.ayush.readinghabit.controller;

import com.ayush.readinghabit.dto.DashboardDTO;
import com.ayush.readinghabit.service.AuthenticatedUserService;
import com.ayush.readinghabit.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final AuthenticatedUserService authenticatedUserService;

    public DashboardController(
            DashboardService dashboardService,
            AuthenticatedUserService authenticatedUserService) {

        this.dashboardService = dashboardService;
        this.authenticatedUserService =
                authenticatedUserService;
    }

    @GetMapping("/my")
    public ResponseEntity<DashboardDTO> getMyDashboard() {

        Long currentUserId =
                authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                dashboardService.getDashboard(currentUserId)
        );
    }
}
