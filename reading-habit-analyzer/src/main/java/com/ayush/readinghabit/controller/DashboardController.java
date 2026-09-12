package com.ayush.readinghabit.controller;

import com.ayush.readinghabit.dto.DashboardDTO;
import com.ayush.readinghabit.service.AuthenticatedUserService;
import com.ayush.readinghabit.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/dashboard")
@Tag(
        name = "Dashboard",
        description = "Aggregated reading dashboard"
)
@SecurityRequirement(name = "bearerAuth")
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
