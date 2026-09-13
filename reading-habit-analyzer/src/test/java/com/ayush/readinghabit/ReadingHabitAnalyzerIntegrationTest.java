//package com.ayush.readinghabit;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Testcontainers
//@ActiveProfiles("test")
//class ReadingHabitAnalyzerIntegrationTest {
//
//    @Container
//    static MySQLContainer<?> mysql =
//            new MySQLContainer<>("mysql:8.4")
//                    .withDatabaseName("reading_habit_test")
//                    .withUsername("test")
//                    .withPassword("test");
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @DynamicPropertySource
//    static void configureDatabase(
//            DynamicPropertyRegistry registry) {
//
//        registry.add(
//                "spring.datasource.url",
//                mysql::getJdbcUrl
//        );
//
//        registry.add(
//                "spring.datasource.username",
//                mysql::getUsername
//        );
//
//        registry.add(
//                "spring.datasource.password",
//                mysql::getPassword
//        );
//
//        registry.add(
//                "spring.datasource.driver-class-name",
//                mysql::getDriverClassName
//        );
//    }
//
//    @Test
//    void applicationContextLoads() {
//    }
//
//    @Test
//    void testProtectedEndpointWithoutToken() throws Exception {
//
//        mockMvc.perform(
//                        get("/api/books")
//                )
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void testUserRegistration() throws Exception {
//
//        String requestBody = """
//                {
//                    "name": "Integration Test User",
//                    "email": "integration@test.com",
//                    "password": "password123"
//                }
//                """;
//
//        mockMvc.perform(
//                        post("/api/users")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(requestBody)
//                )
//                .andExpect(status().isCreated());
//    }
//}