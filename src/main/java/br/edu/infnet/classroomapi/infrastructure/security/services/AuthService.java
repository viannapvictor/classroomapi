package br.edu.infnet.classroomapi.infrastructure.security.services;

import br.edu.infnet.classroomapi.domain.entities.Professor;
import br.edu.infnet.classroomapi.domain.repositories.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthResponse login(String email, String password) {
        Professor professor = professorRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(password, professor.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String accessToken = jwtTokenService.generateToken(professor);
        String refreshToken = jwtTokenService.generateRefreshToken(professor);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(28800)
                .professor(ProfessorInfo.builder()
                        .id(professor.getId())
                        .name(professor.getName())
                        .email(professor.getEmail())
                        .role(professor.getRole())
                        .build())
                .build();
    }

    public Professor register(String name, String email, String password) {
        if (professorRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        Professor professor = new Professor(name, email, encodedPassword);

        return professorRepository.save(professor);
    }

    public AuthResponse refreshToken(String refreshToken) {
        return null;
    }
}