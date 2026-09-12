package com.ayush.readinghabit.service;

import com.ayush.readinghabit.dto.LoginRequestDTO;
import com.ayush.readinghabit.dto.LoginResponseDTO;
import com.ayush.readinghabit.entity.User;
import com.ayush.readinghabit.exception.AuthenticationException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private User user;
    private LoginRequestDTO request;

    @BeforeEach
    void setUp() {

        user = new User(
                "Ayush",
                "ayush@example.com",
                "encodedPassword"
        );

        user.setId(1L);

        request = new LoginRequestDTO();

        request.setEmail("ayush@example.com");
        request.setPassword("password123");
    }

    @Test
    void login_shouldReturnTokenForValidCredentials() {

        when(userRepository.findByEmail(
                request.getEmail()
        )).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )).thenReturn(true);

        when(jwtService.generateToken(
                user.getId(),
                user.getEmail()
        )).thenReturn("jwt-token");

        LoginResponseDTO response =
                authService.login(request);

        assertNotNull(response);

        assertEquals(
                1L,
                response.getUserId()
        );

        assertEquals(
                "Ayush",
                response.getName()
        );

        assertEquals(
                "ayush@example.com",
                response.getEmail()
        );

        assertEquals(
                "jwt-token",
                response.getToken()
        );

        assertEquals(
                "Login successful",
                response.getMessage()
        );

        verify(userRepository)
                .findByEmail(request.getEmail());

        verify(passwordEncoder)
                .matches(
                        request.getPassword(),
                        user.getPassword()
                );

        verify(jwtService)
                .generateToken(
                        user.getId(),
                        user.getEmail()
                );
    }

    @Test
    void login_shouldThrowExceptionWhenEmailDoesNotExist() {

        when(userRepository.findByEmail(
                request.getEmail()
        )).thenReturn(Optional.empty());

        assertThrows(
                AuthenticationException.class,
                () -> authService.login(request)
        );

        verify(userRepository)
                .findByEmail(request.getEmail());

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never())
                .generateToken(anyLong(), anyString());
    }

    @Test
    void login_shouldThrowExceptionWhenPasswordIsWrong() {

        when(userRepository.findByEmail(
                request.getEmail()
        )).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )).thenReturn(false);

        assertThrows(
                AuthenticationException.class,
                () -> authService.login(request)
        );

        verify(passwordEncoder)
                .matches(
                        request.getPassword(),
                        user.getPassword()
                );

        verify(jwtService, never())
                .generateToken(anyLong(), anyString());
    }
}
