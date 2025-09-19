package br.edu.infnet.classroomapi.infrastructure.web.controllers;

import br.edu.infnet.classroomapi.application.dto.request.CreateProfessorRequestDTO;
import br.edu.infnet.classroomapi.application.dto.request.LoginRequestDTO;
import br.edu.infnet.classroomapi.domain.enums.UserRole;
import br.edu.infnet.classroomapi.infrastructure.security.services.AuthResponse;
import br.edu.infnet.classroomapi.infrastructure.security.services.AuthService;
import br.edu.infnet.classroomapi.infrastructure.security.services.ProfessorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequestDTO loginRequest;
    private CreateProfessorRequestDTO createProfessorRequest;
    private AuthResponse authResponse;
    private ProfessorInfo professorInfo;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequestDTO(
            "professor@email.com",
            "password123"
        );

        createProfessorRequest = new CreateProfessorRequestDTO(
            "Prof. João Silva",
            "professor@email.com",
            "password123"
        );

        professorInfo = ProfessorInfo.builder()
            .id(1L)
            .name("Prof. João Silva")
            .email("professor@email.com")
            .role(UserRole.PROFESSOR)
            .build();

        authResponse = AuthResponse.builder()
            .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcm9mZXNzb3JAZW1haWwuY29tIiwiaWF0IjoxNjk5Mjc3ODAwLCJleHAiOjE2OTkzMDY2MDB9.test")
            .refreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcm9mZXNzb3JAZW1haWwuY29tIiwiaWF0IjoxNjk5Mjc3ODAwLCJleHAiOjE3MDA0ODc0MDB9.refresh")
            .tokenType("Bearer")
            .expiresIn(28800)
            .professor(professorInfo)
            .build();
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with valid credentials")
        void shouldLoginSuccessfullyWithValidCredentials() throws Exception {
            when(authService.login(anyString(), anyString())).thenReturn(authResponse);

            mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value(authResponse.getAccessToken()))
                .andExpect(jsonPath("$.data.refreshToken").value(authResponse.getRefreshToken()))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.expiresIn").value(28800))
                .andExpect(jsonPath("$.data.professor.id").value(1L))
                .andExpect(jsonPath("$.data.professor.name").value("Prof. João Silva"))
                .andExpect(jsonPath("$.data.professor.email").value("professor@email.com"))
                .andExpect(jsonPath("$.data.professor.role").value("PROFESSOR"))
                .andExpect(jsonPath("$.message").value("Login successful"));
        }

        @Test
        @DisplayName("Should return validation error for invalid email format")
        void shouldReturnValidationErrorForInvalidEmailFormat() throws Exception {
            LoginRequestDTO invalidRequest = new LoginRequestDTO(
                "invalid-email",
                "password123"
            );

            mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return validation error for empty email")
        void shouldReturnValidationErrorForEmptyEmail() throws Exception {
            LoginRequestDTO invalidRequest = new LoginRequestDTO(
                "",
                "password123"
            );

            mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return validation error for empty password")
        void shouldReturnValidationErrorForEmptyPassword() throws Exception {
            LoginRequestDTO invalidRequest = new LoginRequestDTO(
                "professor@email.com",
                ""
            );

            mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return validation error for null fields")
        void shouldReturnValidationErrorForNullFields() throws Exception {
            LoginRequestDTO invalidRequest = new LoginRequestDTO(null, null);

            mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Register Tests")
    class RegisterTests {

        @Test
        @DisplayName("Should register professor successfully")
        void shouldRegisterProfessorSuccessfully() throws Exception {
            when(authService.login(anyString(), anyString())).thenReturn(authResponse);

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createProfessorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value(authResponse.getAccessToken()))
                .andExpect(jsonPath("$.data.refreshToken").value(authResponse.getRefreshToken()))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.expiresIn").value(28800))
                .andExpect(jsonPath("$.data.professor.id").value(1L))
                .andExpect(jsonPath("$.data.professor.name").value("Prof. João Silva"))
                .andExpect(jsonPath("$.data.professor.email").value("professor@email.com"))
                .andExpect(jsonPath("$.data.professor.role").value("PROFESSOR"))
                .andExpect(jsonPath("$.message").value("Registration successful"));
        }

        @Test
        @DisplayName("Should return validation error for invalid name")
        void shouldReturnValidationErrorForInvalidName() throws Exception {
            CreateProfessorRequestDTO invalidRequest = new CreateProfessorRequestDTO(
                "",
                "professor@email.com",
                "password123"
            );

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return validation error for invalid email format")
        void shouldReturnValidationErrorForInvalidEmailFormat() throws Exception {
            CreateProfessorRequestDTO invalidRequest = new CreateProfessorRequestDTO(
                "Prof. João Silva",
                "invalid-email",
                "password123"
            );

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return validation error for short password")
        void shouldReturnValidationErrorForShortPassword() throws Exception {
            CreateProfessorRequestDTO invalidRequest = new CreateProfessorRequestDTO(
                "Prof. João Silva",
                "professor@email.com",
                "123"
            );

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return validation error for too long name")
        void shouldReturnValidationErrorForTooLongName() throws Exception {
            String longName = "A".repeat(101);
            CreateProfessorRequestDTO invalidRequest = new CreateProfessorRequestDTO(
                longName,
                "professor@email.com",
                "password123"
            );

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return validation error for too long email")
        void shouldReturnValidationErrorForTooLongEmail() throws Exception {
            String longEmail = "a".repeat(90) + "@email.com";
            CreateProfessorRequestDTO invalidRequest = new CreateProfessorRequestDTO(
                "Prof. João Silva",
                longEmail,
                "password123"
            );

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return validation error for null fields")
        void shouldReturnValidationErrorForNullFields() throws Exception {
            CreateProfessorRequestDTO invalidRequest = new CreateProfessorRequestDTO(
                null, null, null
            );

            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }
    }
}
