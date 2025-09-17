package br.edu.infnet.classroomapi.application.services;

import br.edu.infnet.classroomapi.application.dto.request.AssignGradeRequestDTO;
import br.edu.infnet.classroomapi.application.dto.request.CreateEnrollmentRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.EnrollmentResponseDTO;
import br.edu.infnet.classroomapi.application.mappers.EnrollmentDTOMapper;
import br.edu.infnet.classroomapi.domain.entities.Enrollment;
import br.edu.infnet.classroomapi.domain.entities.Student;
import br.edu.infnet.classroomapi.domain.entities.Subject;
import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
import br.edu.infnet.classroomapi.domain.repositories.EnrollmentRepository;
import br.edu.infnet.classroomapi.domain.repositories.StudentRepository;
import br.edu.infnet.classroomapi.domain.repositories.SubjectRepository;
import br.edu.infnet.classroomapi.domain.services.EnrollmentDomainService;
import br.edu.infnet.classroomapi.infrastructure.security.services.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentApplicationService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final EnrollmentDTOMapper enrollmentMapper;
    private final SecurityContextService securityContextService;

    public EnrollmentResponseDTO createEnrollment(CreateEnrollmentRequestDTO request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Long currentProfessorId = securityContextService.getCurrentProfessorId();
        if (!subject.getProfessor().getId().equals(currentProfessorId)) {
            throw new RuntimeException("You can only enroll students in your own subjects");
        }

        if (enrollmentRepository.existsByStudentIdAndSubjectId(request.getStudentId(), request.getSubjectId())) {
            throw new IllegalArgumentException("Student is already enrolled in this subject");
        }

        Enrollment enrollment = EnrollmentDomainService.createEnrollment(student, subject);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return enrollmentMapper.toResponseDTO(savedEnrollment);
    }

    public EnrollmentResponseDTO assignGrade(Long enrollmentId, AssignGradeRequestDTO request) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        Long currentProfessorId = securityContextService.getCurrentProfessorId();
        if (!enrollment.getSubject().getProfessor().getId().equals(currentProfessorId)) {
            throw new RuntimeException("You can only assign grades to your own subjects");
        }

        EnrollmentDomainService.assignGrade(enrollment, request.getGrade());
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return enrollmentMapper.toResponseDTO(savedEnrollment);
    }

    @Transactional(readOnly = true)
    public EnrollmentResponseDTO findById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        return enrollmentMapper.toResponseDTO(enrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> findAll() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        return enrollmentMapper.toResponseDTOList(enrollments);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> findByStudentId(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollmentMapper.toResponseDTOList(enrollments);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> findBySubjectId(Long subjectId) {
        List<Enrollment> enrollments = enrollmentRepository.findBySubjectId(subjectId);
        return enrollmentMapper.toResponseDTOList(enrollments);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> findByCurrentProfessorSubjects() {
        Long professorId = securityContextService.getCurrentProfessorId();
        List<Subject> professorSubjects = subjectRepository.findByProfessorId(professorId);

        return professorSubjects.stream()
                .flatMap(subject -> enrollmentRepository.findBySubjectId(subject.getId()).stream())
                .map(enrollmentMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> findByStatus(EnrollmentStatus status) {
        List<Enrollment> enrollments = enrollmentRepository.findByStatus(status);
        return enrollmentMapper.toResponseDTOList(enrollments);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> findApprovedBySubjectId(Long subjectId) {
        List<Enrollment> enrollments = enrollmentRepository.findApprovedBySubjectId(subjectId);
        return enrollmentMapper.toResponseDTOList(enrollments);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> findReprobedBySubjectId(Long subjectId) {
        List<Enrollment> enrollments = enrollmentRepository.findReprobedBySubjectId(subjectId);
        return enrollmentMapper.toResponseDTOList(enrollments);
    }

    public EnrollmentResponseDTO suspendEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        Long currentProfessorId = securityContextService.getCurrentProfessorId();
        if (!enrollment.getSubject().getProfessor().getId().equals(currentProfessorId)) {
            throw new RuntimeException("You can only manage enrollments in your own subjects");
        }

        enrollment.suspend();
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return enrollmentMapper.toResponseDTO(savedEnrollment);
    }

    public EnrollmentResponseDTO reactivateEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        Long currentProfessorId = securityContextService.getCurrentProfessorId();
        if (!enrollment.getSubject().getProfessor().getId().equals(currentProfessorId)) {
            throw new RuntimeException("You can only manage enrollments in your own subjects");
        }

        enrollment.reactivate();
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return enrollmentMapper.toResponseDTO(savedEnrollment);
    }

    public void deleteEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        Long currentProfessorId = securityContextService.getCurrentProfessorId();
        if (!enrollment.getSubject().getProfessor().getId().equals(currentProfessorId)) {
            throw new RuntimeException("You can only delete enrollments in your own subjects");
        }

        enrollmentRepository.deleteById(enrollmentId);
    }
}