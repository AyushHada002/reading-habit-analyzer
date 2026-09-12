package com.ayush.readinghabit.controller;

import com.ayush.readinghabit.dto.LoginRequestDTO;
import com.ayush.readinghabit.dto.LoginResponseDTO;
import com.ayush.readinghabit.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "Authentication",
        description = "User registration and authentication APIs"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Login user",
            description = "Authenticates a user and returns a JWT token"
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }
}
