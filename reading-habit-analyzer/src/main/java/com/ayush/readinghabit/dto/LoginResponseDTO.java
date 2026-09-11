package com.ayush.readinghabit.dto;

public class LoginResponseDTO {

    private Long userId;
    private String name;
    private String email;
    private String token;
    private String message;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(
            Long userId,
            String name,
            String email,
            String token,
            String message) {

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.token = token;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
}