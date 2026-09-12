package com.ayush.readinghabit.service;

import com.ayush.readinghabit.dto.ReadingGoalRequestDTO;
import com.ayush.readinghabit.dto.ReadingGoalResponseDTO;
import com.ayush.readinghabit.entity.ReadingGoal;
import com.ayush.readinghabit.entity.User;
import com.ayush.readinghabit.exception.BusinessRuleException;
import com.ayush.readinghabit.exception.DuplicateResourceException;
import com.ayush.readinghabit.exception.ResourceNotFoundException;
import com.ayush.readinghabit.repository.ReadingGoalRepository;
import com.ayush.readinghabit.repository.ReadingSessionRepository;
import com.ayush.readinghabit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadingGoalServiceTest {

    @Mock
    private ReadingGoalRepository readingGoalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReadingSessionRepository readingSessionRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private ReadingGoalService readingGoalService;

    private User user;
    private ReadingGoal goal;
    private ReadingGoalRequestDTO request;

    @BeforeEach
    void setUp() {

        user = new User(
                "Ayush",
                "ayush@example.com",
                "encodedPassword"
        );

        user.setId(1L);

        goal = new ReadingGoal();

        goal.setId(50L);
        goal.setMonth(
                YearMonth.of(2026, 9)
        );
        goal.setTargetPages(500);
        goal.setTargetMinutes(1000);
        goal.setUser(user);

        request = new ReadingGoalRequestDTO();

        request.setMonth(
                YearMonth.of(2026, 9)
        );

        request.setTargetPages(500);

        request.setTargetMinutes(1000);

        request.setUserId(1L);
    }

    // =========================================================
    // 1. SUCCESSFUL CREATE
    // =========================================================

    @Test
    void createGoal_shouldCreateSuccessfully() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(readingGoalRepository.existsByUserIdAndMonth(
                1L,
                YearMonth.of(2026, 9)
        )).thenReturn(false);

        when(readingGoalRepository.save(
                any(ReadingGoal.class)
        )).thenReturn(goal);

        ReadingGoalResponseDTO response =
                readingGoalService.createGoal(request);

        assertNotNull(response);

        assertEquals(
                50L,
                response.getId()
        );

        assertEquals(
                YearMonth.of(2026, 9),
                response.getMonth()
        );

        assertEquals(
                500,
                response.getTargetPages()
        );

        assertEquals(
                1000,
                response.getTargetMinutes()
        );

        assertEquals(
                1L,
                response.getUserId()
        );

        verify(userRepository)
                .findById(1L);

        verify(readingGoalRepository)
                .existsByUserIdAndMonth(
                        1L,
                        YearMonth.of(2026, 9)
                );

        verify(readingGoalRepository)
                .save(any(ReadingGoal.class));
    }

    // =========================================================
    // 2. DUPLICATE MONTHLY GOAL
    // =========================================================

    @Test
    void createGoal_shouldRejectDuplicateMonthlyGoal() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(readingGoalRepository.existsByUserIdAndMonth(
                1L,
                YearMonth.of(2026, 9)
        )).thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> readingGoalService.createGoal(request)
        );

        verify(userRepository)
                .findById(1L);

        verify(readingGoalRepository)
                .existsByUserIdAndMonth(
                        1L,
                        YearMonth.of(2026, 9)
                );

        verify(readingGoalRepository, never())
                .save(any(ReadingGoal.class));
    }

    // =========================================================
    // 3. UNKNOWN USER
    // =========================================================

    @Test
    void createGoal_shouldRejectUnknownUser() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> readingGoalService.createGoal(request)
        );

        verify(userRepository)
                .findById(1L);

        verify(readingGoalRepository, never())
                .existsByUserIdAndMonth(
                        anyLong(),
                        any(YearMonth.class)
                );

        verify(readingGoalRepository, never())
                .save(any(ReadingGoal.class));
    }

    // =========================================================
    // 4. ZERO TARGET PAGES
    // =========================================================

    @Test
    void createGoal_shouldRejectZeroTargetPages() {

        request.setTargetPages(0);

        assertThrows(
                BusinessRuleException.class,
                () -> readingGoalService.createGoal(request)
        );

        verify(userRepository, never())
                .findById(anyLong());

        verify(readingGoalRepository, never())
                .save(any(ReadingGoal.class));
    }

    // =========================================================
    // 5. ZERO TARGET MINUTES
    // =========================================================

    @Test
    void createGoal_shouldRejectZeroTargetMinutes() {

        request.setTargetMinutes(0);

        assertThrows(
                BusinessRuleException.class,
                () -> readingGoalService.createGoal(request)
        );

        verify(userRepository, never())
                .findById(anyLong());

        verify(readingGoalRepository, never())
                .save(any(ReadingGoal.class));
    }

    // =========================================================
    // 6. NEGATIVE TARGET PAGES
    // =========================================================

    @Test
    void createGoal_shouldRejectNegativeTargetPages() {

        request.setTargetPages(-1);

        assertThrows(
                BusinessRuleException.class,
                () -> readingGoalService.createGoal(request)
        );

        verify(userRepository, never())
                .findById(anyLong());

        verify(readingGoalRepository, never())
                .save(any(ReadingGoal.class));
    }

    // =========================================================
    // 7. NEGATIVE TARGET MINUTES
    // =========================================================

    @Test
    void createGoal_shouldRejectNegativeTargetMinutes() {

        request.setTargetMinutes(-1);

        assertThrows(
                BusinessRuleException.class,
                () -> readingGoalService.createGoal(request)
        );

        verify(userRepository, never())
                .findById(anyLong());

        verify(readingGoalRepository, never())
                .save(any(ReadingGoal.class));
    }
}