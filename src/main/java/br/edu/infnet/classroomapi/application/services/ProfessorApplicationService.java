package br.edu.infnet.classroomapi.application.services;

import br.edu.infnet.classroomapi.application.dto.request.CreateProfessorRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.ProfessorResponseDTO;
import br.edu.infnet.classroomapi.application.mappers.ProfessorDTOMapper;
import br.edu.infnet.classroomapi.domain.entities.Professor;
import br.edu.infnet.classroomapi.domain.repositories.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfessorApplicationService {

    private final ProfessorRepository professorRepository;
    private final ProfessorDTOMapper professorMapper;
    private final PasswordEncoder passwordEncoder;

    public ProfessorResponseDTO createProfessor(CreateProfessorRequestDTO request) {
        if (professorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Professor professor = professorMapper.toDomain(request);
        professor.setPassword(passwordEncoder.encode(request.getPassword()));

        Professor savedProfessor = professorRepository.save(professor);
        return professorMapper.toResponseDTO(savedProfessor);
    }

    @Transactional(readOnly = true)
    public ProfessorResponseDTO findById(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor not found with id: " + id));

        return professorMapper.toResponseDTO(professor);
    }

    @Transactional(readOnly = true)
    public ProfessorResponseDTO findByEmail(String email) {
        Professor professor = professorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Professor not found with email: " + email));

        return professorMapper.toResponseDTO(professor);
    }

    @Transactional(readOnly = true)
    public List<ProfessorResponseDTO> findAll() {
        List<Professor> professors = professorRepository.findAll();
        return professorMapper.toResponseDTOList(professors);
    }

    @Transactional(readOnly = true)
    public List<ProfessorResponseDTO> findByName(String name) {
        List<Professor> professors = professorRepository.findByNameContainingIgnoreCase(name);
        return professorMapper.toResponseDTOList(professors);
    }

    public void deleteById(Long id) {
        if (!professorRepository.findById(id).isPresent()) {
            throw new RuntimeException("Professor not found with id: " + id);
        }
        professorRepository.deleteById(id);
    }
}