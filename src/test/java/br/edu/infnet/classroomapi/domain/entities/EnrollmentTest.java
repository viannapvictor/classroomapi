package br.edu.infnet.classroomapi.domain.entities;

import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Enrollment Entity Tests")
class EnrollmentTest {

    private Enrollment enrollment;
    private Student student;
    private Subject subject;

    @BeforeEach
    void setUp() {
        String cpf = "12345678901";
        Address address = new Address("Rua A", "100", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");
        student = new Student("João Silva", cpf, "joao@email.com", "(11) 99999-9999", address);

        Professor professor = new Professor("Prof. Silva", "prof@email.com", "password");
        subject = new Subject("Java Programming", "JAVA101", "Introduction to Java", 60);
        subject.setProfessor(professor);

        enrollment = new Enrollment(student, subject);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create enrollment with valid data")
        void shouldCreateEnrollmentWithValidData() {
            assertNotNull(enrollment);
            assertEquals(student, enrollment.getStudent());
            assertEquals(subject, enrollment.getSubject());
            assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
            assertNull(enrollment.getGrade());
            assertNotNull(enrollment.getEnrollmentDate());
            assertNull(enrollment.getCompletionDate());
            assertNotNull(enrollment.getCreatedAt());
            assertNotNull(enrollment.getUpdatedAt());
        }

        @Test
        @DisplayName("Should set initial status as ACTIVE")
        void shouldSetInitialStatusAsActive() {
            assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
            assertTrue(enrollment.isActive());
            assertFalse(enrollment.isCompleted());
        }

        @Test
        @DisplayName("Should set enrollment date to current time")
        void shouldSetEnrollmentDateToCurrentTime() {
            LocalDateTime before = LocalDateTime.now().minusSeconds(1);
            LocalDateTime after = LocalDateTime.now().plusSeconds(1);

            assertNotNull(enrollment.getEnrollmentDate());
            assertTrue(enrollment.getEnrollmentDate().isAfter(before));
            assertTrue(enrollment.getEnrollmentDate().isBefore(after));
        }
    }

    @Nested
    @DisplayName("Grade Assignment Tests")
    class GradeAssignmentTests {

        @Test
        @DisplayName("Should assign grade successfully for passing grade")
        void shouldAssignGradeSuccessfullyForPassingGrade() {
            BigDecimal passingGrade = new BigDecimal("8.5");

            enrollment.assignGrade(passingGrade);

            assertEquals(passingGrade, enrollment.getGrade());
            assertEquals(EnrollmentStatus.COMPLETED, enrollment.getStatus());
            assertNotNull(enrollment.getCompletionDate());
            assertTrue(enrollment.isApproved());
            assertTrue(enrollment.isCompleted());
        }

        @Test
        @DisplayName("Should assign grade successfully for failing grade")
        void shouldAssignGradeSuccessfullyForFailingGrade() {
            BigDecimal failingGrade = new BigDecimal("5.0");

            enrollment.assignGrade(failingGrade);

            assertEquals(failingGrade, enrollment.getGrade());
            assertEquals(EnrollmentStatus.COMPLETED, enrollment.getStatus());
            assertNotNull(enrollment.getCompletionDate());
            assertFalse(enrollment.isApproved());
            assertTrue(enrollment.isCompleted());
        }

        @Test
        @DisplayName("Should assign grade at minimum passing threshold")
        void shouldAssignGradeAtMinimumPassingThreshold() {
            BigDecimal minimumPassingGrade = new BigDecimal("7.0");

            enrollment.assignGrade(minimumPassingGrade);

            assertEquals(minimumPassingGrade, enrollment.getGrade());
            assertEquals(EnrollmentStatus.COMPLETED, enrollment.getStatus());
            assertTrue(enrollment.isApproved());
        }

        @Test
        @DisplayName("Should reject null grade")
        void shouldRejectNullGrade() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> enrollment.assignGrade(null)
            );

            assertEquals("Grade must be between 0 and 10", exception.getMessage());
        }

        @Test
        @DisplayName("Should reject grade below zero")
        void shouldRejectGradeBelowZero() {
            BigDecimal invalidGrade = new BigDecimal("-1.0");

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> enrollment.assignGrade(invalidGrade)
            );

            assertEquals("Grade must be between 0 and 10", exception.getMessage());
        }

        @Test
        @DisplayName("Should reject grade above ten")
        void shouldRejectGradeAboveTen() {
            BigDecimal invalidGrade = new BigDecimal("11.0");

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> enrollment.assignGrade(invalidGrade)
            );

            assertEquals("Grade must be between 0 and 10", exception.getMessage());
        }

        @Test
        @DisplayName("Should accept grade at boundaries")
        void shouldAcceptGradeAtBoundaries() {
            enrollment.assignGrade(BigDecimal.ZERO);
            assertEquals(BigDecimal.ZERO, enrollment.getGrade());

            enrollment.reactivate();

            enrollment.assignGrade(BigDecimal.TEN);
            assertEquals(BigDecimal.TEN, enrollment.getGrade());
        }
    }

    @Nested
    @DisplayName("Status Management Tests")
    class StatusManagementTests {

        @Test
        @DisplayName("Should complete enrollment successfully")
        void shouldCompleteEnrollmentSuccessfully() {
            enrollment.complete();

            assertEquals(EnrollmentStatus.COMPLETED, enrollment.getStatus());
            assertNotNull(enrollment.getCompletionDate());
            assertTrue(enrollment.isCompleted());
        }

        @Test
        @DisplayName("Should suspend enrollment successfully")
        void shouldSuspendEnrollmentSuccessfully() {
            enrollment.suspend();

            assertEquals(EnrollmentStatus.SUSPENDED, enrollment.getStatus());
            assertFalse(enrollment.isActive());
            assertFalse(enrollment.isCompleted());
        }

        @Test
        @DisplayName("Should cancel enrollment successfully")
        void shouldCancelEnrollmentSuccessfully() {
            enrollment.cancel();

            assertEquals(EnrollmentStatus.CANCELLED, enrollment.getStatus());
            assertFalse(enrollment.isActive());
            assertFalse(enrollment.isCompleted());
        }

        @Test
        @DisplayName("Should reactivate enrollment successfully")
        void shouldReactivateEnrollmentSuccessfully() {
            enrollment.suspend();
            assertFalse(enrollment.isActive());

            enrollment.reactivate();

            assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
            assertTrue(enrollment.isActive());
            assertFalse(enrollment.isCompleted());
        }
    }

    @Nested
    @DisplayName("Approval Tests")
    class ApprovalTests {

        @Test
        @DisplayName("Should return false for approval when no grade assigned")
        void shouldReturnFalseForApprovalWhenNoGradeAssigned() {
            assertFalse(enrollment.isApproved());
        }

        @Test
        @DisplayName("Should return true for approval with passing grade")
        void shouldReturnTrueForApprovalWithPassingGrade() {
            enrollment.assignGrade(new BigDecimal("8.0"));
            assertTrue(enrollment.isApproved());
        }

        @Test
        @DisplayName("Should return false for approval with failing grade")
        void shouldReturnFalseForApprovalWithFailingGrade() {
            enrollment.assignGrade(new BigDecimal("6.0"));
            assertFalse(enrollment.isApproved());
        }
    }

    @Nested
    @DisplayName("Update Timestamp Tests")
    class UpdateTimestampTests {

        @Test
        @DisplayName("Should update timestamp when status changes")
        void shouldUpdateTimestampWhenStatusChanges() throws InterruptedException {
            LocalDateTime originalTimestamp = enrollment.getUpdatedAt();

            Thread.sleep(10);
            enrollment.suspend();

            assertTrue(enrollment.getUpdatedAt().isAfter(originalTimestamp));
        }

        @Test
        @DisplayName("Should update timestamp when grade is assigned")
        void shouldUpdateTimestampWhenGradeIsAssigned() throws InterruptedException {
            LocalDateTime originalTimestamp = enrollment.getUpdatedAt();

            Thread.sleep(10);
            enrollment.assignGrade(new BigDecimal("8.0"));

            assertTrue(enrollment.getUpdatedAt().isAfter(originalTimestamp));
        }
    }
}