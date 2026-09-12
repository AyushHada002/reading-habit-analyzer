//package com.ayush.readinghabit.controller;
//
//import com.ayush.readinghabit.dto.UserResponseDTO;
//import com.ayush.readinghabit.exception.DuplicateResourceException;
//import com.ayush.readinghabit.exception.GlobalExceptionHandler;
//import com.ayush.readinghabit.exception.ResourceNotFoundException;
//import com.ayush.readinghabit.service.UserService;
//
//import org.junit.jupiter.api.Test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import com.ayush.readinghabit.security.JwtAuthenticationFilter;
//import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@AutoConfigureMockMvc(addFilters = false)
//@WebMvcTest(UserController.class)
//@Import(GlobalExceptionHandler.class)
//class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @MockitoBean
//    private UserService userService;
//
//    @Test
//    void createUser_shouldReturn201() throws Exception {
//
//        UserResponseDTO response =
//                new UserResponseDTO(
//                        1L,
//                        "Ayush",
//                        "ayush@example.com",
//                        LocalDateTime.now()
//                );
//
//        when(userService.createUser(any()))
//                .thenReturn(response);
//
//        String requestBody = """
//                {
//                    "name": "Ayush",
//                    "email": "ayush@example.com",
//                    "password": "password123"
//                }
//                """;
//
//        mockMvc.perform(
//                        post("/api/users")
//                                .contentType(
//                                        MediaType.APPLICATION_JSON
//                                )
//                                .content(requestBody)
//                )
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id")
//                        .value(1))
//                .andExpect(jsonPath("$.name")
//                        .value("Ayush"))
//                .andExpect(jsonPath("$.email")
//                        .value("ayush@example.com"));
//
//        verify(userService)
//                .createUser(any());
//    }
//    @Test
//    void createUser_shouldRejectInvalidEmail() throws Exception {
//
//        String requestBody = """
//            {
//                "name": "Ayush",
//                "email": "invalid-email",
//                "password": "password123"
//            }
//            """;
//
//        mockMvc.perform(
//                        post("/api/users")
//                                .contentType(
//                                        MediaType.APPLICATION_JSON
//                                )
//                                .content(requestBody)
//                )
//                .andExpect(status().isBadRequest());
//
//        verify(userService, never())
//                .createUser(any());
//    }
//
//    @Test
//    void createUser_shouldRejectMissingName() throws Exception {
//
//        String requestBody = """
//            {
//                "name": "",
//                "email": "ayush@example.com",
//                "password": "password123"
//            }
//            """;
//
//        mockMvc.perform(
//                        post("/api/users")
//                                .contentType(
//                                        MediaType.APPLICATION_JSON
//                                )
//                                .content(requestBody)
//                )
//                .andExpect(status().isBadRequest());
//
//        verify(userService, never())
//                .createUser(any());
//    }
//
//    @Test
//    void createUser_shouldRejectShortPassword() throws Exception {
//
//        String requestBody = """
//            {
//                "name": "Ayush",
//                "email": "ayush@example.com",
//                "password": "123"
//            }
//            """;
//
//        mockMvc.perform(
//                        post("/api/users")
//                                .contentType(
//                                        MediaType.APPLICATION_JSON
//                                )
//                                .content(requestBody)
//                )
//                .andExpect(status().isBadRequest());
//
//        verify(userService, never())
//                .createUser(any());
//    }
//
//    @Test
//    void createUser_shouldReturn409WhenEmailExists()
//            throws Exception {
//
//        when(userService.createUser(any()))
//                .thenThrow(
//                        new DuplicateResourceException(
//                                "Email already exists"
//                        )
//                );
//
//        String requestBody = """
//            {
//                "name": "Ayush",
//                "email": "ayush@example.com",
//                "password": "password123"
//            }
//            """;
//
//        mockMvc.perform(
//                        post("/api/users")
//                                .contentType(
//                                        MediaType.APPLICATION_JSON
//                                )
//                                .content(requestBody)
//                )
//                .andExpect(status().isConflict())
//                .andExpect(jsonPath("$.status")
//                        .value(409))
//                .andExpect(jsonPath("$.error")
//                        .value("DUPLICATE_RESOURCE"));
//    }
//
//    @Test
//    void getAllUsers_shouldReturn200() throws Exception {
//
//        UserResponseDTO user =
//                new UserResponseDTO(
//                        1L,
//                        "Ayush",
//                        "ayush@example.com",
//                        LocalDateTime.now()
//                );
//
//        when(userService.getAllUsers())
//                .thenReturn(List.of(user));
//
//        mockMvc.perform(
//                        get("/api/users")
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id")
//                        .value(1))
//                .andExpect(jsonPath("$[0].name")
//                        .value("Ayush"))
//                .andExpect(jsonPath("$[0].email")
//                        .value("ayush@example.com"));
//
//        verify(userService)
//                .getAllUsers();
//    }@Test
//    void getUserById_shouldReturn200() throws Exception {
//
//        UserResponseDTO user =
//                new UserResponseDTO(
//                        1L,
//                        "Ayush",
//                        "ayush@example.com",
//                        LocalDateTime.now()
//                );
//
//        when(userService.getUserById(1L))
//                .thenReturn(user);
//
//        mockMvc.perform(
//                        get("/api/users/1")
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id")
//                        .value(1))
//                .andExpect(jsonPath("$.name")
//                        .value("Ayush"));
//
//        verify(userService)
//                .getUserById(1L);
//    }
//
//    @Test
//    void getUserById_shouldReturn404WhenNotFound()
//            throws Exception {
//
//        when(userService.getUserById(99L))
//                .thenThrow(
//                        new ResourceNotFoundException(
//                                "User not found with id: 99"
//                        )
//                );
//
//        mockMvc.perform(
//                        get("/api/users/99")
//                )
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status")
//                        .value(404))
//                .andExpect(jsonPath("$.error")
//                        .value("RESOURCE_NOT_FOUND"));
//    }
//
//    @Test
//    void deleteUser_shouldReturn204() throws Exception {
//
//        doNothing()
//                .when(userService)
//                .deleteUser(1L);
//
//        mockMvc.perform(
//                        delete("/api/users/1")
//                )
//                .andExpect(status().isNoContent());
//
//        verify(userService)
//                .deleteUser(1L);
//    }
//
//
//}
