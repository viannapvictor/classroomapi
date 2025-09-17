package br.edu.infnet.classroomapi.application.services;

import br.edu.infnet.classroomapi.application.dto.request.CreateSubjectRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectSummaryDTO;
import br.edu.infnet.classroomapi.application.mappers.SubjectDTOMapper;
import br.edu.infnet.classroomapi.domain.entities.Professor;
import br.edu.infnet.classroomapi.domain.entities.Subject;
import br.edu.infnet.classroomapi.domain.repositories.ProfessorRepository;
import br.edu.infnet.classroomapi.domain.repositories.SubjectRepository;
import br.edu.infnet.classroomapi.infrastructure.security.services.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectApplicationService {

    private final SubjectRepository subjectRepository;
    private final ProfessorRepository professorRepository;
    private final SubjectDTOMapper subjectMapper;
    private final SecurityContextService securityContextService;

    public SubjectResponseDTO createSubject(CreateSubjectRequestDTO request) {
        if (subjectRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Subject code already exists");
        }

        Long professorId = securityContextService.getCurrentProfessorId();
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        Subject subject = subjectMapper.toDomain(request);
        subject.setProfessor(professor);

        Subject savedSubject = subjectRepository.save(subject);
        return subjectMapper.toResponseDTO(savedSubject);
    }

    @Transactional(readOnly = true)
    public SubjectResponseDTO findById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));

        return subjectMapper.toResponseDTO(subject);
    }

    @Transactional(readOnly = true)
    public SubjectResponseDTO findByCode(String code) {
        Subject subject = subjectRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Subject not found with code: " + code));

        return subjectMapper.toResponseDTO(subject);
    }

    @Transactional(readOnly = true)
    public List<SubjectResponseDTO> findAll() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjectMapper.toResponseDTOList(subjects);
    }

    @Transactional(readOnly = true)
    public List<SubjectSummaryDTO> findAllSummary() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjectMapper.toSummaryDTOList(subjects);
    }

    @Transactional(readOnly = true)
    public List<SubjectResponseDTO> findByName(String name) {
        List<Subject> subjects = subjectRepository.findByNameContainingIgnoreCase(name);
        return subjectMapper.toResponseDTOList(subjects);
    }

    @Transactional(readOnly = true)
    public List<SubjectResponseDTO> findByCurrentProfessor() {
        Long professorId = securityContextService.getCurrentProfessorId();
        List<Subject> subjects = subjectRepository.findByProfessorId(professorId);
        return subjectMapper.toResponseDTOList(subjects);
    }

    @Transactional(readOnly = true)
    public List<SubjectResponseDTO> findByProfessorId(Long professorId) {
        List<Subject> subjects = subjectRepository.findByProfessorId(professorId);
        return subjectMapper.toResponseDTOList(subjects);
    }

    public SubjectResponseDTO updateSubject(Long id, CreateSubjectRequestDTO request) {
        Subject existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));

        Long currentProfessorId = securityContextService.getCurrentProfessorId();
        if (!existingSubject.getProfessor().getId().equals(currentProfessorId)) {
            throw new RuntimeException("You can only update your own subjects");
        }

        if (!existingSubject.getCode().equals(request.getCode()) &&
            subjectRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Subject code already exists");
        }

        Subject updatedSubject = subjectMapper.toDomain(request);
        updatedSubject.setId(id);
        updatedSubject.setProfessor(existingSubject.getProfessor());
        updatedSubject.setCreatedAt(existingSubject.getCreatedAt());

        Subject savedSubject = subjectRepository.save(updatedSubject);
        return subjectMapper.toResponseDTO(savedSubject);
    }

    public void deleteById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));

        Long currentProfessorId = securityContextService.getCurrentProfessorId();
        if (!subject.getProfessor().getId().equals(currentProfessorId)) {
            throw new RuntimeException("You can only delete your own subjects");
        }

        subjectRepository.deleteById(id);
    }
}