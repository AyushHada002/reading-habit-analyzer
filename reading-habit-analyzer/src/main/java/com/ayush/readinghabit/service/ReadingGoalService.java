package com.ayush.readinghabit.service;

import com.ayush.readinghabit.dto.ReadingGoalRequestDTO;
import com.ayush.readinghabit.dto.ReadingGoalResponseDTO;
import com.ayush.readinghabit.entity.ReadingGoal;
import com.ayush.readinghabit.entity.User;
import com.ayush.readinghabit.exception.DuplicateResourceException;
import com.ayush.readinghabit.exception.ResourceNotFoundException;
import com.ayush.readinghabit.repository.ReadingGoalRepository;
import com.ayush.readinghabit.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.ayush.readinghabit.dto.ReadingGoalProgressDTO;
import com.ayush.readinghabit.repository.ReadingSessionRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReadingGoalService {

    private final ReadingGoalRepository readingGoalRepository;
    private final UserRepository userRepository;
    private final ReadingSessionRepository readingSessionRepository;

    public ReadingGoalService(
            ReadingGoalRepository readingGoalRepository,
            UserRepository userRepository,
            ReadingSessionRepository readingSessionRepository) {

        this.readingGoalRepository = readingGoalRepository;
        this.userRepository = userRepository;
        this.readingSessionRepository = readingSessionRepository;
    }

    public ReadingGoalResponseDTO createGoal(
            ReadingGoalRequestDTO request) {

        User user = findUser(request.getUserId());

        if (readingGoalRepository.existsByUserIdAndMonth(
                request.getUserId(),
                request.getMonth())) {

            throw new DuplicateResourceException(
                    "Reading goal already exists for "
                            + request.getMonth()
                            + " for user id: "
                            + request.getUserId()
            );
        }

        ReadingGoal goal = new ReadingGoal();

        goal.setMonth(request.getMonth());
        goal.setTargetPages(request.getTargetPages());
        goal.setTargetMinutes(request.getTargetMinutes());
        goal.setUser(user);

        ReadingGoal savedGoal =
                readingGoalRepository.save(goal);

        return convertToResponseDTO(savedGoal);
    }

    public List<ReadingGoalResponseDTO> getGoalsByUserId(
            Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return readingGoalRepository
                .findByUserIdOrderByMonthDesc(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public ReadingGoalResponseDTO getGoalById(Long id) {

        ReadingGoal goal =
                readingGoalRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reading goal not found with id: "
                                                + id
                                )
                        );

        return convertToResponseDTO(goal);
    }

    public ReadingGoalResponseDTO updateGoal(
            Long id,
            ReadingGoalRequestDTO request) {

        ReadingGoal existingGoal =
                readingGoalRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reading goal not found with id: "
                                                + id
                                )
                        );

        User user = findUser(request.getUserId());

        boolean duplicateExists =
                readingGoalRepository
                        .existsByUserIdAndMonth(
                                request.getUserId(),
                                request.getMonth()
                        );

        if (duplicateExists) {

            ReadingGoal duplicateGoal =
                    readingGoalRepository
                            .findByUserIdAndMonth(
                                    request.getUserId(),
                                    request.getMonth()
                            )
                            .orElse(null);

            if (duplicateGoal != null
                    && !duplicateGoal.getId()
                    .equals(existingGoal.getId())) {

                throw new DuplicateResourceException(
                        "Reading goal already exists for "
                                + request.getMonth()
                                + " for user id: "
                                + request.getUserId()
                );
            }
        }

        existingGoal.setMonth(request.getMonth());
        existingGoal.setTargetPages(request.getTargetPages());
        existingGoal.setTargetMinutes(request.getTargetMinutes());
        existingGoal.setUser(user);

        ReadingGoal updatedGoal =
                readingGoalRepository.save(existingGoal);

        return convertToResponseDTO(updatedGoal);
    }

    public void deleteGoal(Long id) {

        ReadingGoal existingGoal =
                readingGoalRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reading goal not found with id: "
                                                + id
                                )
                        );

        readingGoalRepository.delete(existingGoal);
    }

    private User findUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );
    }

    private ReadingGoalResponseDTO convertToResponseDTO(
            ReadingGoal goal) {

        return new ReadingGoalResponseDTO(
                goal.getId(),
                goal.getMonth(),
                goal.getTargetPages(),
                goal.getTargetMinutes(),
                goal.getUser().getId()
        );
    }
    public ReadingGoalProgressDTO getGoalProgress(Long goalId) {

        ReadingGoal goal =
                readingGoalRepository.findById(goalId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reading goal not found with id: "
                                                + goalId
                                )
                        );

        Long userId = goal.getUser().getId();

        LocalDate startDate =
                goal.getMonth().atDay(1);

        LocalDate endDate =
                goal.getMonth().atEndOfMonth();

        long pagesRead =
                readingSessionRepository
                        .sumPagesReadByUserIdAndDateRange(
                                userId,
                                startDate,
                                endDate
                        );

        long minutesRead =
                readingSessionRepository
                        .sumDurationMinutesByUserIdAndDateRange(
                                userId,
                                startDate,
                                endDate
                        );

        double pageProgress =
                calculatePercentage(
                        pagesRead,
                        goal.getTargetPages()
                );

        double timeProgress =
                calculatePercentage(
                        minutesRead,
                        goal.getTargetMinutes()
                );

        double overallProgress =
                (pageProgress + timeProgress) / 2;

        return new ReadingGoalProgressDTO(
                goal.getId(),
                userId,
                goal.getMonth(),
                goal.getTargetPages(),
                pagesRead,
                roundToTwoDecimals(pageProgress),
                goal.getTargetMinutes(),
                minutesRead,
                roundToTwoDecimals(timeProgress),
                roundToTwoDecimals(overallProgress)
        );
    }

    private double calculatePercentage(
            long actual,
            int target) {

        if (target <= 0) {
            return 0.0;
        }

        double percentage =
                ((double) actual / target) * 100;

        return Math.min(percentage, 100.0);
    }

    private double roundToTwoDecimals(double value) {

        return Math.round(value * 100.0) / 100.0;
    }
}
