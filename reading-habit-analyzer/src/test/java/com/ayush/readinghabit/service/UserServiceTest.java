package com.ayush.readinghabit.service;

import com.ayush.readinghabit.dto.UserRequestDTO;
import com.ayush.readinghabit.dto.UserResponseDTO;
import com.ayush.readinghabit.entity.User;
import com.ayush.readinghabit.exception.DuplicateResourceException;
import com.ayush.readinghabit.exception.ResourceNotFoundException;
import com.ayush.readinghabit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRequestDTO request;
    private User user;

    @BeforeEach
    void setUp() {

        request = new UserRequestDTO();

        request.setName("Ayush");
        request.setEmail("ayush@example.com");
        request.setPassword("password123");

        user = new User(
                "Ayush",
                "ayush@example.com",
                "encodedPassword"
        );

        user.setId(1L);
    }
    @Test
    void createUser_shouldCreateUserSuccessfully() {

        when(userRepository.findByEmail(
                request.getEmail()
        )).thenReturn(Optional.empty());

        when(passwordEncoder.encode(
                request.getPassword()
        )).thenReturn("encodedPassword");

        when(userRepository.save(
                any(User.class)
        )).thenReturn(user);

        UserResponseDTO response =
                userService.createUser(request);

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Ayush",
                response.getName()
        );

        assertEquals(
                "ayush@example.com",
                response.getEmail()
        );

        verify(userRepository)
                .findByEmail(request.getEmail());

        verify(passwordEncoder)
                .encode(request.getPassword());

        verify(userRepository)
                .save(any(User.class));
    }

    @Test
    void createUser_shouldThrowExceptionWhenEmailExists() {

        when(userRepository.findByEmail(
                request.getEmail()
        )).thenReturn(Optional.of(user));

        assertThrows(
                DuplicateResourceException.class,
                () -> userService.createUser(request)
        );

        verify(userRepository)
                .findByEmail(request.getEmail());

        verify(userRepository, never())
                .save(any(User.class));

        verify(passwordEncoder, never())
                .encode(anyString());
    }

    @Test
    void getUserById_shouldReturnUser() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponseDTO response =
                userService.getUserById(1L);

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Ayush",
                response.getName()
        );

        assertEquals(
                "ayush@example.com",
                response.getEmail()
        );

        verify(userRepository)
                .findById(1L);
    }

    @Test
    void getUserById_shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(99L)
        );

        verify(userRepository)
                .findById(99L);
    }

    @Test
    void deleteUser_shouldDeleteExistingUser() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository)
                .findById(1L);

        verify(userRepository)
                .delete(user);
    }

    @Test
    void deleteUser_shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.deleteUser(99L)
        );

        verify(userRepository, never())
                .delete(any(User.class));
    }
}
