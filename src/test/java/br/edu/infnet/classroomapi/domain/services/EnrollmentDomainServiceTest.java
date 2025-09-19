package br.edu.infnet.classroomapi.domain.services;

import br.edu.infnet.classroomapi.domain.entities.Enrollment;
import br.edu.infnet.classroomapi.domain.entities.Professor;
import br.edu.infnet.classroomapi.domain.entities.Student;
import br.edu.infnet.classroomapi.domain.entities.Subject;
import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
import br.edu.infnet.classroomapi.domain.entities.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("EnrollmentDomainService Tests")
class EnrollmentDomainServiceTest {

    private Student student;
    private Subject subject;
    private Professor professor;

    @BeforeEach
    void setUp() {
        String cpf = "12345678901";
        Address address = new Address("Rua A", "100", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");
        student = new Student("João Silva", cpf, "joao@email.com", "(11) 99999-9999", address);
        student.setId(1L);

        professor = new Professor("Prof. Silva", "prof@email.com", "password");
        professor.setId(1L);
        subject = new Subject("Java Programming", "JAVA101", "Introduction to Java", 60);
        subject.setProfessor(professor);
        subject.setId(1L);
    }

    @Nested
    @DisplayName("Create Enrollment Tests")
    class CreateEnrollmentTests {

        @Test
        @DisplayName("Should create enrollment successfully with valid student and subject")
        void shouldCreateEnrollmentSuccessfullyWithValidStudentAndSubject() {
            Enrollment enrollment = EnrollmentDomainService.createEnrollment(student, subject);

            assertNotNull(enrollment);
            assertEquals(student, enrollment.getStudent());
            assertEquals(subject, enrollment.getSubject());
            assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
            assertNull(enrollment.getGrade());

            assertTrue(student.getEnrollments().contains(enrollment));
            assertTrue(subject.getEnrollments().contains(enrollment));
        }

        @Test
        @DisplayName("Should throw exception when student is null")
        void shouldThrowExceptionWhenStudentIsNull() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrollmentDomainService.createEnrollment(null, subject)
            );

            assertEquals("Student cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when subject is null")
        void shouldThrowExceptionWhenSubjectIsNull() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrollmentDomainService.createEnrollment(student, null)
            );

            assertEquals("Subject cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when student is already enrolled in subject")
        void shouldThrowExceptionWhenStudentIsAlreadyEnrolledInSubject() {
            EnrollmentDomainService.createEnrollment(student, subject);

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrollmentDomainService.createEnrollment(student, subject)
            );

            assertEquals("Student is already enrolled in this subject", exception.getMessage());
        }

        @Test
        @DisplayName("Should allow enrollment after previous enrollment is not active")
        void shouldAllowEnrollmentAfterPreviousEnrollmentIsNotActive() {
            Enrollment firstEnrollment = EnrollmentDomainService.createEnrollment(student, subject);

            firstEnrollment.complete();

            assertDoesNotThrow(() -> {
                Enrollment secondEnrollment = EnrollmentDomainService.createEnrollment(student, subject);
                assertNotNull(secondEnrollment);
                assertEquals(EnrollmentStatus.ACTIVE, secondEnrollment.getStatus());
            });
        }
    }

    @Nested
    @DisplayName("Assign Grade Tests")
    class AssignGradeTests {

        private Enrollment enrollment;

        @BeforeEach
        void setUp() {
            enrollment = EnrollmentDomainService.createEnrollment(student, subject);
        }

        @Test
        @DisplayName("Should assign grade successfully to active enrollment")
        void shouldAssignGradeSuccessfullyToActiveEnrollment() {
            BigDecimal grade = new BigDecimal("8.5");

            assertDoesNotThrow(() -> EnrollmentDomainService.assignGrade(enrollment, grade));

            assertEquals(grade, enrollment.getGrade());
            assertEquals(EnrollmentStatus.COMPLETED, enrollment.getStatus());
            assertTrue(enrollment.isApproved());
        }

        @Test
        @DisplayName("Should assign failing grade successfully")
        void shouldAssignFailingGradeSuccessfully() {
            BigDecimal grade = new BigDecimal("5.0");

            assertDoesNotThrow(() -> EnrollmentDomainService.assignGrade(enrollment, grade));

            assertEquals(grade, enrollment.getGrade());
            assertEquals(EnrollmentStatus.COMPLETED, enrollment.getStatus());
            assertFalse(enrollment.isApproved());
        }

        @Test
        @DisplayName("Should throw exception when enrollment is null")
        void shouldThrowExceptionWhenEnrollmentIsNull() {
            BigDecimal grade = new BigDecimal("8.0");

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrollmentDomainService.assignGrade(null, grade)
            );

            assertEquals("Enrollment cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when grade is null")
        void shouldThrowExceptionWhenGradeIsNull() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrollmentDomainService.assignGrade(enrollment, null)
            );

            assertEquals("Grade cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when enrollment is not active")
        void shouldThrowExceptionWhenEnrollmentIsNotActive() {
            enrollment.complete();
            BigDecimal grade = new BigDecimal("8.0");

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrollmentDomainService.assignGrade(enrollment, grade)
            );

            assertEquals("Can only assign grades to active enrollments", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for grade below zero")
        void shouldThrowExceptionForGradeBelowZero() {
            BigDecimal invalidGrade = new BigDecimal("-1.0");

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrollmentDomainService.assignGrade(enrollment, invalidGrade)
            );

            assertEquals("Grade must be between 0 and 10", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for grade above ten")
        void shouldThrowExceptionForGradeAboveTen() {
            BigDecimal invalidGrade = new BigDecimal("11.0");

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrollmentDomainService.assignGrade(enrollment, invalidGrade)
            );

            assertEquals("Grade must be between 0 and 10", exception.getMessage());
        }

        @Test
        @DisplayName("Should accept grade at boundaries")
        void shouldAcceptGradeAtBoundaries() {
            assertDoesNotThrow(() -> EnrollmentDomainService.assignGrade(enrollment, BigDecimal.ZERO));

            String cpf2 = "98765432109";
            Address address2 = new Address("Rua B", "200", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");
            Student student2 = new Student("Maria Silva", cpf2, "maria@email.com", "(11) 88888-8888", address2);
            student2.setId(2L);

            Subject subject2 = new Subject("Python Programming", "PY101", "Introduction to Python", 60);
            subject2.setProfessor(professor);
            subject2.setId(2L);
            Enrollment enrollment2 = EnrollmentDomainService.createEnrollment(student2, subject2);

            assertDoesNotThrow(() -> EnrollmentDomainService.assignGrade(enrollment2, BigDecimal.TEN));
        }
    }

    @Nested
    @DisplayName("Can Enroll Tests")
    class CanEnrollTests {

        @Test
        @DisplayName("Should return true when student can enroll in subject")
        void shouldReturnTrueWhenStudentCanEnrollInSubject() {
            boolean canEnroll = EnrollmentDomainService.canEnroll(student, subject);

            assertTrue(canEnroll);
        }

        @Test
        @DisplayName("Should return false when student is null")
        void shouldReturnFalseWhenStudentIsNull() {
            boolean canEnroll = EnrollmentDomainService.canEnroll(null, subject);

            assertFalse(canEnroll);
        }

        @Test
        @DisplayName("Should return false when subject is null")
        void shouldReturnFalseWhenSubjectIsNull() {
            boolean canEnroll = EnrollmentDomainService.canEnroll(student, null);

            assertFalse(canEnroll);
        }

        @Test
        @DisplayName("Should return false when student is already actively enrolled")
        void shouldReturnFalseWhenStudentIsAlreadyActivelyEnrolled() {
            EnrollmentDomainService.createEnrollment(student, subject);

            boolean canEnroll = EnrollmentDomainService.canEnroll(student, subject);

            assertFalse(canEnroll);
        }

        @Test
        @DisplayName("Should return true when previous enrollment is completed")
        void shouldReturnTrueWhenPreviousEnrollmentIsCompleted() {
            Enrollment enrollment = EnrollmentDomainService.createEnrollment(student, subject);
            enrollment.complete();

            boolean canEnroll = EnrollmentDomainService.canEnroll(student, subject);

            assertTrue(canEnroll);
        }

        @Test
        @DisplayName("Should return true when previous enrollment is suspended")
        void shouldReturnTrueWhenPreviousEnrollmentIsSuspended() {
            Enrollment enrollment = EnrollmentDomainService.createEnrollment(student, subject);
            enrollment.suspend();

            boolean canEnroll = EnrollmentDomainService.canEnroll(student, subject);

            assertTrue(canEnroll);
        }

        @Test
        @DisplayName("Should return true when previous enrollment is cancelled")
        void shouldReturnTrueWhenPreviousEnrollmentIsCancelled() {
            Enrollment enrollment = EnrollmentDomainService.createEnrollment(student, subject);
            enrollment.cancel();

            boolean canEnroll = EnrollmentDomainService.canEnroll(student, subject);

            assertTrue(canEnroll);
        }
    }
}