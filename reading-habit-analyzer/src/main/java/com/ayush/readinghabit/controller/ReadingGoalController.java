package com.ayush.readinghabit.controller;

import com.ayush.readinghabit.dto.ReadingGoalRequestDTO;
import com.ayush.readinghabit.dto.ReadingGoalResponseDTO;
import com.ayush.readinghabit.service.ReadingGoalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ayush.readinghabit.dto.ReadingGoalProgressDTO;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class ReadingGoalController {

    private final ReadingGoalService readingGoalService;

    public ReadingGoalController(
            ReadingGoalService readingGoalService) {

        this.readingGoalService = readingGoalService;
    }

    @PostMapping
    public ResponseEntity<ReadingGoalResponseDTO> createGoal(
            @Valid @RequestBody ReadingGoalRequestDTO request) {

        ReadingGoalResponseDTO createdGoal =
                readingGoalService.createGoal(request);

        return new ResponseEntity<>(
                createdGoal,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadingGoalResponseDTO>> getGoalsByUserId(
            @PathVariable Long userId) {

        List<ReadingGoalResponseDTO> goals =
                readingGoalService.getGoalsByUserId(userId);

        return ResponseEntity.ok(goals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadingGoalResponseDTO> getGoalById(
            @PathVariable Long id) {

        ReadingGoalResponseDTO goal =
                readingGoalService.getGoalById(id);

        return ResponseEntity.ok(goal);
    }

    @GetMapping("/{id}/progress")
    public ResponseEntity<ReadingGoalProgressDTO> getGoalProgress(
            @PathVariable Long id) {

        ReadingGoalProgressDTO progress =
                readingGoalService.getGoalProgress(id);

        return ResponseEntity.ok(progress);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadingGoalResponseDTO> updateGoal(
            @PathVariable Long id,
            @Valid @RequestBody ReadingGoalRequestDTO request) {

        ReadingGoalResponseDTO updatedGoal =
                readingGoalService.updateGoal(id, request);

        return ResponseEntity.ok(updatedGoal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(
            @PathVariable Long id) {

        readingGoalService.deleteGoal(id);

        return ResponseEntity.noContent().build();
    }
}