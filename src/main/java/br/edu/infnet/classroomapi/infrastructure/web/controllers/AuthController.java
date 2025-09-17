package br.edu.infnet.classroomapi.infrastructure.web.controllers;

import br.edu.infnet.classroomapi.application.dto.request.CreateProfessorRequestDTO;
import br.edu.infnet.classroomapi.application.dto.request.LoginRequestDTO;
import br.edu.infnet.classroomapi.infrastructure.security.services.AuthResponse;
import br.edu.infnet.classroomapi.infrastructure.security.services.AuthService;
import br.edu.infnet.classroomapi.infrastructure.web.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication operations")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login professor", description = "Authenticate professor and return JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponse authResponse = authService.login(request.getEmail(), request.getPassword());
        ApiResponse<AuthResponse> response = ApiResponse.success(authResponse, "Login successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register professor", description = "Register new professor account")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody CreateProfessorRequestDTO request) {
        authService.register(request.getName(), request.getEmail(), request.getPassword());

        AuthResponse authResponse = authService.login(request.getEmail(), request.getPassword());
        ApiResponse<AuthResponse> response = ApiResponse.success(authResponse, "Registration successful");
        return ResponseEntity.ok(response);
    }
}