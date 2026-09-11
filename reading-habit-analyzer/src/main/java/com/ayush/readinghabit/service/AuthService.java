package com.ayush.readinghabit.service;

import com.ayush.readinghabit.dto.LoginRequestDTO;
import com.ayush.readinghabit.dto.LoginResponseDTO;
import com.ayush.readinghabit.entity.User;
import com.ayush.readinghabit.exception.AuthenticationException;
import com.ayush.readinghabit.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ayush.readinghabit.service.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public LoginResponseDTO login(
            LoginRequestDTO request) {

        User user =
                userRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new AuthenticationException(
                                        "Invalid email or password"
                                )
                        );

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new AuthenticationException(
                    "Invalid email or password"
            );
        }

        String token =
                jwtService.generateToken(
                        user.getId(),
                        user.getEmail()
                );

        return new LoginResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                token,
                "Login successful"
        );
    }
}
