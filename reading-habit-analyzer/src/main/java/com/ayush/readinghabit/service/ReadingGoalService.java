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

import java.util.List;

@Service
public class ReadingGoalService {

    private final ReadingGoalRepository readingGoalRepository;
    private final UserRepository userRepository;

    public ReadingGoalService(
            ReadingGoalRepository readingGoalRepository,
            UserRepository userRepository) {

        this.readingGoalRepository = readingGoalRepository;
        this.userRepository = userRepository;
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
}
