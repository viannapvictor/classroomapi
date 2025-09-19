package br.edu.infnet.classroomapi.application.services;

import br.edu.infnet.classroomapi.application.dto.request.AssignGradeRequestDTO;
import br.edu.infnet.classroomapi.application.dto.request.CreateEnrollmentRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.EnrollmentResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectResponseDTO;
import br.edu.infnet.classroomapi.application.mappers.EnrollmentDTOMapper;
import br.edu.infnet.classroomapi.domain.entities.Enrollment;
import br.edu.infnet.classroomapi.domain.entities.Professor;
import br.edu.infnet.classroomapi.domain.entities.Student;
import br.edu.infnet.classroomapi.domain.entities.Subject;
import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
import br.edu.infnet.classroomapi.domain.enums.UserRole;
import br.edu.infnet.classroomapi.domain.repositories.EnrollmentRepository;
import br.edu.infnet.classroomapi.domain.repositories.StudentRepository;
import br.edu.infnet.classroomapi.domain.repositories.SubjectRepository;
import br.edu.infnet.classroomapi.infrastructure.persistence.repositories.EnrollmentJpaRepository;
import br.edu.infnet.classroomapi.infrastructure.security.services.SecurityContextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("EnrollmentApplicationService Tests")
class EnrollmentApplicationServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private EnrollmentDTOMapper enrollmentMapper;

    @Mock
    private EnrollmentJpaRepository enrollmentJpaRepository;

    @Mock
    private SecurityContextService securityContextService;

    @InjectMocks
    private EnrollmentApplicationService enrollmentService;

    private CreateEnrollmentRequestDTO createEnrollmentRequest;
    private AssignGradeRequestDTO assignGradeRequest;
    private Student student;
    private Subject subject;
    private Professor professor;
    private Enrollment enrollment;
    private EnrollmentResponseDTO enrollmentResponseDTO;

    @BeforeEach
    void setUp() {
        professor = new Professor();
        professor.setId(1L);
        professor.setName("Prof. Silva");
        professor.setEmail("prof@email.com");
        professor.setPassword("password123");
        professor.setRole(UserRole.PROFESSOR);
        professor.setCreatedAt(LocalDateTime.now());
        professor.setUpdatedAt(LocalDateTime.now());
        professor.setSubjects(new ArrayList<>());

        student = new Student();
        student.setId(1L);
        student.setName("João Silva");
        student.setCpf("12345678901");
        student.setEmail("joao@email.com");
        student.setPhone("(11) 99999-9999");
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        student.setEnrollments(new ArrayList<>());

        subject = new Subject();
        subject.setId(1L);
        subject.setName("Java Programming");
        subject.setCode("JAVA101");
        subject.setDescription("Introduction to Java");
        subject.setWorkload(60);
        subject.setProfessor(professor);
        subject.setCreatedAt(LocalDateTime.now());
        subject.setUpdatedAt(LocalDateTime.now());
        subject.setEnrollments(new ArrayList<>());

        enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudent(student);
        enrollment.setSubject(subject);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setGrade(BigDecimal.valueOf(8.5));
        enrollment.setEnrollmentDate(LocalDateTime.now());

        createEnrollmentRequest = new CreateEnrollmentRequestDTO(1L, 1L);
        assignGradeRequest = new AssignGradeRequestDTO(BigDecimal.valueOf(8.5));

        enrollmentResponseDTO = EnrollmentResponseDTO.builder()
                .id(1L)
                .student(StudentResponseDTO.builder().id(1L).name("João Silva").build())
                .subject(SubjectResponseDTO.builder().id(1L).name("Java Programming").build())
                .status(EnrollmentStatus.ACTIVE)
                .grade(BigDecimal.valueOf(8.5))
                .enrollmentDate(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("Create Enrollment Tests")
    class CreateEnrollmentTests {

        @Test
        @DisplayName("Should create enrollment successfully")
        void shouldCreateEnrollmentSuccessfully() {
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
            when(securityContextService.getCurrentProfessorId()).thenReturn(1L);
            when(enrollmentRepository.existsByStudentIdAndSubjectId(1L, 1L)).thenReturn(false);
            when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
            when(enrollmentMapper.toResponseDTO(any(Enrollment.class), any(EnrollmentJpaRepository.class)))
                    .thenReturn(enrollmentResponseDTO);

            EnrollmentResponseDTO result = enrollmentService.createEnrollment(createEnrollmentRequest);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("João Silva", result.getStudent().getName());
            assertEquals("Java Programming", result.getSubject().getName());
            assertEquals(EnrollmentStatus.ACTIVE, result.getStatus());

            verify(studentRepository).findById(1L);
            verify(subjectRepository).findById(1L);
            verify(securityContextService).getCurrentProfessorId();
            verify(enrollmentRepository).existsByStudentIdAndSubjectId(1L, 1L);
            verify(enrollmentRepository).save(any(Enrollment.class));
        }

        @Test
        @DisplayName("Should throw exception when student not found")
        void shouldThrowExceptionWhenStudentNotFound() {
            when(studentRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> enrollmentService.createEnrollment(createEnrollmentRequest));

            assertEquals("Student not found", exception.getMessage());
            verify(studentRepository).findById(1L);
            verifyNoInteractions(subjectRepository, enrollmentRepository);
        }

        @Test
        @DisplayName("Should throw exception when subject not found")
        void shouldThrowExceptionWhenSubjectNotFound() {
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> enrollmentService.createEnrollment(createEnrollmentRequest));

            assertEquals("Subject not found", exception.getMessage());
            verify(studentRepository).findById(1L);
            verify(subjectRepository).findById(1L);
            verifyNoInteractions(enrollmentRepository);
        }

        @Test
        @DisplayName("Should throw exception when professor is not owner of subject")
        void shouldThrowExceptionWhenProfessorIsNotOwnerOfSubject() {
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
            when(securityContextService.getCurrentProfessorId()).thenReturn(2L);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> enrollmentService.createEnrollment(createEnrollmentRequest));

            assertEquals("You can only enroll students in your own subjects", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when student already enrolled")
        void shouldThrowExceptionWhenStudentAlreadyEnrolled() {
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
            when(securityContextService.getCurrentProfessorId()).thenReturn(1L);
            when(enrollmentRepository.existsByStudentIdAndSubjectId(1L, 1L)).thenReturn(true);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> enrollmentService.createEnrollment(createEnrollmentRequest));

            assertEquals("Student is already enrolled in this subject", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Assign Grade Tests")
    class AssignGradeTests {

        @Test
        @DisplayName("Should assign grade successfully")
        void shouldAssignGradeSuccessfully() {
            when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
            when(securityContextService.getCurrentProfessorId()).thenReturn(1L);
            when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
            when(enrollmentMapper.toResponseDTO(any(Enrollment.class), any(EnrollmentJpaRepository.class)))
                    .thenReturn(enrollmentResponseDTO);

            EnrollmentResponseDTO result = enrollmentService.assignGrade(1L, assignGradeRequest);

            assertNotNull(result);
            assertEquals(BigDecimal.valueOf(8.5), result.getGrade());

            verify(enrollmentRepository).findById(1L);
            verify(securityContextService).getCurrentProfessorId();
            verify(enrollmentRepository).save(any(Enrollment.class));
        }

        @Test
        @DisplayName("Should throw exception when enrollment not found")
        void shouldThrowExceptionWhenEnrollmentNotFound() {
            when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> enrollmentService.assignGrade(1L, assignGradeRequest));

            assertEquals("Enrollment not found", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when professor is not owner of subject")
        void shouldThrowExceptionWhenProfessorIsNotOwnerOfSubject() {
            when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
            when(securityContextService.getCurrentProfessorId()).thenReturn(2L);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> enrollmentService.assignGrade(1L, assignGradeRequest));

            assertEquals("You can only assign grades to your own subjects", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Find Enrollment Tests")
    class FindEnrollmentTests {

        @Test
        @DisplayName("Should find enrollment by ID successfully")
        void shouldFindEnrollmentByIdSuccessfully() {
            when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
            when(enrollmentMapper.toResponseDTO(any(Enrollment.class), any(EnrollmentJpaRepository.class)))
                    .thenReturn(enrollmentResponseDTO);

            EnrollmentResponseDTO result = enrollmentService.findById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("João Silva", result.getStudent().getName());

            verify(enrollmentRepository).findById(1L);
        }

        @Test
        @DisplayName("Should throw exception when enrollment not found by ID")
        void shouldThrowExceptionWhenEnrollmentNotFoundById() {
            when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> enrollmentService.findById(1L));

            assertEquals("Enrollment not found", exception.getMessage());
        }

        @Test
        @DisplayName("Should find all enrollments successfully")
        void shouldFindAllEnrollmentsSuccessfully() {
            List<Enrollment> enrollments = Arrays.asList(enrollment);
            when(enrollmentRepository.findAll()).thenReturn(enrollments);
            when(enrollmentMapper.toResponseDTOList(any(), any(EnrollmentJpaRepository.class)))
                    .thenReturn(Arrays.asList(enrollmentResponseDTO));

            List<EnrollmentResponseDTO> result = enrollmentService.findAll();

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());
            assertEquals("João Silva", result.get(0).getStudent().getName());

            verify(enrollmentRepository).findAll();
        }

        @Test
        @DisplayName("Should find enrollments by student ID successfully")
        void shouldFindEnrollmentsByStudentIdSuccessfully() {
            List<Enrollment> enrollments = Arrays.asList(enrollment);
            when(enrollmentRepository.findByStudentId(1L)).thenReturn(enrollments);
            when(enrollmentMapper.toResponseDTOList(any(), any(EnrollmentJpaRepository.class)))
                    .thenReturn(Arrays.asList(enrollmentResponseDTO));

            List<EnrollmentResponseDTO> result = enrollmentService.findByStudentId(1L);

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());

            verify(enrollmentRepository).findByStudentId(1L);
        }

        @Test
        @DisplayName("Should find enrollments by subject ID successfully")
        void shouldFindEnrollmentsBySubjectIdSuccessfully() {
            List<Enrollment> enrollments = Arrays.asList(enrollment);
            when(enrollmentRepository.findBySubjectId(1L)).thenReturn(enrollments);
            when(enrollmentMapper.toResponseDTOList(any(), any(EnrollmentJpaRepository.class)))
                    .thenReturn(Arrays.asList(enrollmentResponseDTO));

            List<EnrollmentResponseDTO> result = enrollmentService.findBySubjectId(1L);

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());

            verify(enrollmentRepository).findBySubjectId(1L);
        }

        @Test
        @DisplayName("Should find enrollments by status successfully")
        void shouldFindEnrollmentsByStatusSuccessfully() {
            List<Enrollment> enrollments = Arrays.asList(enrollment);
            when(enrollmentRepository.findByStatus(EnrollmentStatus.ACTIVE)).thenReturn(enrollments);
            when(enrollmentMapper.toResponseDTOList(any(), any(EnrollmentJpaRepository.class)))
                    .thenReturn(Arrays.asList(enrollmentResponseDTO));

            List<EnrollmentResponseDTO> result = enrollmentService.findByStatus(EnrollmentStatus.ACTIVE);

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());

            verify(enrollmentRepository).findByStatus(EnrollmentStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should find approved enrollments by subject ID successfully")
        void shouldFindApprovedEnrollmentsBySubjectIdSuccessfully() {
            List<Enrollment> enrollments = Arrays.asList(enrollment);
            when(enrollmentRepository.findApprovedBySubjectId(1L)).thenReturn(enrollments);
            when(enrollmentMapper.toResponseDTOList(any(), any(EnrollmentJpaRepository.class)))
                    .thenReturn(Arrays.asList(enrollmentResponseDTO));

            List<EnrollmentResponseDTO> result = enrollmentService.findApprovedBySubjectId(1L);

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());

            verify(enrollmentRepository).findApprovedBySubjectId(1L);
        }

        @Test
        @DisplayName("Should find reproved enrollments by subject ID successfully")
        void shouldFindReprovedEnrollmentsBySubjectIdSuccessfully() {
            List<Enrollment> enrollments = Arrays.asList(enrollment);
            when(enrollmentRepository.findReprobedBySubjectId(1L)).thenReturn(enrollments);
            when(enrollmentMapper.toResponseDTOList(any(), any(EnrollmentJpaRepository.class)))
                    .thenReturn(Arrays.asList(enrollmentResponseDTO));

            List<EnrollmentResponseDTO> result = enrollmentService.findReprobedBySubjectId(1L);

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());

            verify(enrollmentRepository).findReprobedBySubjectId(1L);
        }

        @Test
        @DisplayName("Should find current professor subjects enrollments successfully")
        void shouldFindCurrentProfessorSubjectsEnrollmentsSuccessfully() {
            List<Subject> professorSubjects = Arrays.asList(subject);
            List<Enrollment> enrollments = Arrays.asList(enrollment);

            when(securityContextService.getCurrentProfessorId()).thenReturn(1L);
            when(subjectRepository.findByProfessorId(1L)).thenReturn(professorSubjects);
            when(enrollmentRepository.findBySubjectId(1L)).thenReturn(enrollments);
            when(enrollmentMapper.toResponseDTO(any(Enrollment.class), any(EnrollmentJpaRepository.class)))
                    .thenReturn(enrollmentResponseDTO);

            List<EnrollmentResponseDTO> result = enrollmentService.findByCurrentProfessorSubjects();

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());

            verify(securityContextService).getCurrentProfessorId();
            verify(subjectRepository).findByProfessorId(1L);
            verify(enrollmentRepository).findBySubjectId(1L);
        }
    }

    @Nested
    @DisplayName("Update Enrollment Tests")
    class UpdateEnrollmentTests {

        @Test
        @DisplayName("Should suspend enrollment successfully")
        void shouldSuspendEnrollmentSuccessfully() {
            when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
            when(securityContextService.getCurrentProfessorId()).thenReturn(1L);
            when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
            when(enrollmentMapper.toResponseDTO(any(Enrollment.class), any(EnrollmentJpaRepository.class)))
                    .thenReturn(enrollmentResponseDTO);

            EnrollmentResponseDTO result = enrollmentService.suspendEnrollment(1L);

            assertNotNull(result);

            verify(enrollmentRepository).findById(1L);
            verify(securityContextService).getCurrentProfessorId();
            verify(enrollmentRepository).save(any(Enrollment.class));
        }

        @Test
        @DisplayName("Should reactivate enrollment successfully")
        void shouldReactivateEnrollmentSuccessfully() {
            when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
            when(securityContextService.getCurrentProfessorId()).thenReturn(1L);
            when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
            when(enrollmentMapper.toResponseDTO(any(Enrollment.class), any(EnrollmentJpaRepository.class)))
                    .thenReturn(enrollmentResponseDTO);

            EnrollmentResponseDTO result = enrollmentService.reactivateEnrollment(1L);

            assertNotNull(result);

            verify(enrollmentRepository).findById(1L);
            verify(securityContextService).getCurrentProfessorId();
            verify(enrollmentRepository).save(any(Enrollment.class));
        }

        @Test
        @DisplayName("Should throw exception when trying to manage other professor's enrollment")
        void shouldThrowExceptionWhenTryingToManageOtherProfessorsEnrollment() {
            when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
            when(securityContextService.getCurrentProfessorId()).thenReturn(2L);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> enrollmentService.suspendEnrollment(1L));

            assertEquals("You can only manage enrollments in your own subjects", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Delete Enrollment Tests")
    class DeleteEnrollmentTests {

        @Test
        @DisplayName("Should delete enrollment successfully")
        void shouldDeleteEnrollmentSuccessfully() {
            when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
            when(securityContextService.getCurrentProfessorId()).thenReturn(1L);

            assertDoesNotThrow(() -> enrollmentService.deleteEnrollment(1L));

            verify(enrollmentRepository).findById(1L);
            verify(securityContextService).getCurrentProfessorId();
            verify(enrollmentRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw exception when enrollment not found for deletion")
        void shouldThrowExceptionWhenEnrollmentNotFoundForDeletion() {
            when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> enrollmentService.deleteEnrollment(1L));

            assertEquals("Enrollment not found", exception.getMessage());
            verify(enrollmentRepository).findById(1L);
            verify(enrollmentRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Should throw exception when trying to delete other professor's enrollment")
        void shouldThrowExceptionWhenTryingToDeleteOtherProfessorsEnrollment() {
            when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
            when(securityContextService.getCurrentProfessorId()).thenReturn(2L);
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> enrollmentService.deleteEnrollment(1L));

            assertEquals("You can only delete enrollments in your own subjects", exception.getMessage());
            verify(enrollmentRepository, never()).deleteById(anyLong());
        }
    }
}
