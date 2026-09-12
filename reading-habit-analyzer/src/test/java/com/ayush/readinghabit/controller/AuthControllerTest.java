//package com.ayush.readinghabit.controller;
//
//import com.ayush.readinghabit.dto.LoginResponseDTO;
//import com.ayush.readinghabit.exception.AuthenticationException;
//import com.ayush.readinghabit.exception.GlobalExceptionHandler;
//import com.ayush.readinghabit.security.JwtAuthenticationFilter;
//import com.ayush.readinghabit.service.AuthService;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
//import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(AuthController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@Import(GlobalExceptionHandler.class)
//class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private AuthService authService;
//
//    @MockitoBean
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Test
//    void login_shouldReturn200() throws Exception {
//
//        LoginResponseDTO response =
//                new LoginResponseDTO(
//                        1L,
//                        "Ayush",
//                        "ayush@example.com",
//                        "jwt-token",
//                        "Login successful"
//                );
//
//        when(authService.login(any()))
//                .thenReturn(response);
//
//        String requestBody = """
//                {
//                    "email": "ayush@example.com",
//                    "password": "password123"
//                }
//                """;
//
//        mockMvc.perform(
//                        post("/api/auth/login")
//                                .contentType(
//                                        MediaType.APPLICATION_JSON
//                                )
//                                .content(requestBody)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId")
//                        .value(1))
//                .andExpect(jsonPath("$.email")
//                        .value("ayush@example.com"))
//                .andExpect(jsonPath("$.token")
//                        .value("jwt-token"))
//                .andExpect(jsonPath("$.message")
//                        .value("Login successful"));
//
//        verify(authService)
//                .login(any());
//    }
//
//    @Test
//    void login_shouldReturn401ForInvalidCredentials()
//            throws Exception {
//
//        when(authService.login(any()))
//                .thenThrow(
//                        new AuthenticationException(
//                                "Invalid email or password"
//                        )
//                );
//
//        String requestBody = """
//                {
//                    "email": "ayush@example.com",
//                    "password": "wrongpassword"
//                }
//                """;
//
//        mockMvc.perform(
//                        post("/api/auth/login")
//                                .contentType(
//                                        MediaType.APPLICATION_JSON
//                                )
//                                .content(requestBody)
//                )
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.status")
//                        .value(401))
//                .andExpect(jsonPath("$.error")
//                        .value("AUTHENTICATION_FAILED"));
//    }
//}
