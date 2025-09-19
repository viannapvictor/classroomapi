package br.edu.infnet.classroomapi.application.mappers;

import br.edu.infnet.classroomapi.application.dto.response.EnrollmentResponseDTO;
import br.edu.infnet.classroomapi.domain.entities.Address;
import br.edu.infnet.classroomapi.domain.entities.Enrollment;
import br.edu.infnet.classroomapi.domain.entities.Professor;
import br.edu.infnet.classroomapi.domain.entities.Student;
import br.edu.infnet.classroomapi.domain.entities.Subject;
import br.edu.infnet.classroomapi.infrastructure.persistence.repositories.EnrollmentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("EnrollmentDTOMapper Tests")
class EnrollmentDTOMapperTest {

    @Autowired
    private EnrollmentDTOMapper mapper;

    @MockitoBean
    private EnrollmentJpaRepository enrollmentRepository;

    private Enrollment enrollment;
    private Student student;
    private Subject subject;
    private Professor professor;
    private Address address;

    @BeforeEach
    void setUp() {

        address = new Address(
            "Rua das Flores", "123", "Apto 45", "Centro",
            "São Paulo", "SP", "01234-567", "Brasil"
        );

        student = new Student("João Silva", "12345678901", "joao@email.com", "(11) 99999-9999", address);
        student.setId(1L);

        professor = new Professor("Prof. Silva", "prof@email.com", "password");
        professor.setId(1L);

        subject = new Subject("Java Programming", "JAVA101", "Introduction to Java", 60);
        subject.setProfessor(professor);
        subject.setId(1L);

        enrollment = new Enrollment(student, subject);
        enrollment.setId(1L);
        enrollment.assignGrade(BigDecimal.valueOf(8.5));
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("Response DTO Mapping Tests")
    class ResponseDTOMappingTests {

        @Test
        @DisplayName("Should map domain Enrollment to EnrollmentResponseDTO successfully")
        void shouldMapDomainEnrollmentToResponseDTOSuccessfully() {
            when(enrollmentRepository.countBySubjectId(1L)).thenReturn(5L);

            EnrollmentResponseDTO result = mapper.toResponseDTO(enrollment, enrollmentRepository);

            assertNotNull(result);
            assertEquals(enrollment.getId(), result.getId());
            assertEquals(enrollment.isApproved(), result.getApproved());
            assertEquals(enrollment.getCreatedAt(), result.getCreatedAt());
            assertEquals(enrollment.getUpdatedAt(), result.getUpdatedAt());

            assertNotNull(result.getStudent());
            assertEquals(student.getId(), result.getStudent().getId());
            assertEquals(student.getName(), result.getStudent().getName());

            assertNotNull(result.getSubject());
            assertEquals(subject.getId(), result.getSubject().getId());
            assertEquals(subject.getName(), result.getSubject().getName());
            assertEquals(5L, result.getSubject().getEnrolledStudentsCount());
        }

        @Test
        @DisplayName("Should handle null domain Enrollment")
        void shouldHandleNullDomainEnrollment() {
            EnrollmentResponseDTO result = mapper.toResponseDTO(null, enrollmentRepository);
            assertNull(result);
        }

        @Test
        @DisplayName("Should map Enrollment with null student")
        void shouldMapEnrollmentWithNullStudent() {
            enrollment.setStudent(null);

            EnrollmentResponseDTO result = mapper.toResponseDTO(enrollment, enrollmentRepository);

            assertNotNull(result);
            assertEquals(enrollment.getId(), result.getId());
            assertNull(result.getStudent());
            assertNotNull(result.getSubject());
        }

        @Test
        @DisplayName("Should map Enrollment with null subject")
        void shouldMapEnrollmentWithNullSubject() {
            enrollment.setSubject(null);

            EnrollmentResponseDTO result = mapper.toResponseDTO(enrollment, enrollmentRepository);

            assertNotNull(result);
            assertEquals(enrollment.getId(), result.getId());
            assertNotNull(result.getStudent());
            assertNull(result.getSubject());
        }

        @Test
        @DisplayName("Should map Enrollment with approved false")
        void shouldMapEnrollmentWithApprovedFalse() {
            Enrollment failedEnrollment = new Enrollment(student, subject);
            failedEnrollment.setId(2L);
            failedEnrollment.assignGrade(BigDecimal.valueOf(4.0));

            EnrollmentResponseDTO result = mapper.toResponseDTO(failedEnrollment, enrollmentRepository);

            assertNotNull(result);
            assertFalse(result.getApproved());
        }
    }

    @Nested
    @DisplayName("List Mapping Tests")
    class ListMappingTests {

        @Test
        @DisplayName("Should map list of Enrollments to list of EnrollmentResponseDTO successfully")
        void shouldMapListOfEnrollmentsToResponseDTOListSuccessfully() {
            Student student2 = new Student("Maria Silva", "98765432109", "maria@email.com", "(11) 88888-8888", address);
            student2.setId(2L);

            Subject subject2 = new Subject("Python Programming", "PY101", "Introduction to Python", 45);
            subject2.setId(2L);

            Enrollment enrollment2 = new Enrollment(student2, subject2);
            enrollment2.setId(2L);
            enrollment2.assignGrade(BigDecimal.valueOf(5.0));

            List<Enrollment> enrollments = Arrays.asList(enrollment, enrollment2);

            when(enrollmentRepository.countBySubjectId(1L)).thenReturn(3L);
            when(enrollmentRepository.countBySubjectId(2L)).thenReturn(7L);

            List<EnrollmentResponseDTO> result = mapper.toResponseDTOList(enrollments, enrollmentRepository);

            assertNotNull(result);
            assertEquals(2, result.size());

            assertEquals(enrollment.getId(), result.get(0).getId());
            assertEquals(enrollment2.getId(), result.get(1).getId());
            assertTrue(result.get(0).getApproved());
            assertFalse(result.get(1).getApproved());

            assertEquals(student.getName(), result.get(0).getStudent().getName());
            assertEquals(student2.getName(), result.get(1).getStudent().getName());

            assertEquals(subject.getName(), result.get(0).getSubject().getName());
            assertEquals(subject2.getName(), result.get(1).getSubject().getName());
        }

        @Test
        @DisplayName("Should handle null list")
        void shouldHandleNullList() {
            List<EnrollmentResponseDTO> result = mapper.toResponseDTOList(null, enrollmentRepository);
            assertNull(result);
        }

        @Test
        @DisplayName("Should handle empty list")
        void shouldHandleEmptyList() {
            List<Enrollment> emptyList = Arrays.asList();

            List<EnrollmentResponseDTO> result = mapper.toResponseDTOList(emptyList, enrollmentRepository);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}