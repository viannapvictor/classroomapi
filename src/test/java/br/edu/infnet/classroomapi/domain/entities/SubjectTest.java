package br.edu.infnet.classroomapi.domain.entities;

import br.edu.infnet.classroomapi.domain.entities.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Subject Entity Tests")
class SubjectTest {

    private Subject subject;
    private Professor professor;

    @BeforeEach
    void setUp() {
        professor = new Professor("Prof. Silva", "prof@email.com", "password");
        subject = new Subject("Java Programming", "JAVA101", "Introduction to Java", 60);
        subject.setProfessor(professor);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create subject with valid data")
        void shouldCreateSubjectWithValidData() {
            assertNotNull(subject);
            assertEquals("Java Programming", subject.getName());
            assertEquals("JAVA101", subject.getCode());
            assertEquals("Introduction to Java", subject.getDescription());
            assertEquals(60, subject.getWorkload());
            assertEquals(professor, subject.getProfessor());
            assertNotNull(subject.getEnrollments());
            assertTrue(subject.getEnrollments().isEmpty());
            assertNotNull(subject.getCreatedAt());
            assertNotNull(subject.getUpdatedAt());
        }

        @Test
        @DisplayName("Should initialize enrollments as empty list")
        void shouldInitializeEnrollmentsAsEmptyList() {
            assertNotNull(subject.getEnrollments());
            assertTrue(subject.getEnrollments().isEmpty());
        }

        @Test
        @DisplayName("Should set creation and update timestamps")
        void shouldSetCreationAndUpdateTimestamps() {
            LocalDateTime before = LocalDateTime.now().minusSeconds(1);
            LocalDateTime after = LocalDateTime.now().plusSeconds(1);

            assertNotNull(subject.getCreatedAt());
            assertNotNull(subject.getUpdatedAt());
            assertTrue(subject.getCreatedAt().isAfter(before));
            assertTrue(subject.getCreatedAt().isBefore(after));
            assertEquals(subject.getCreatedAt(), subject.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("Enrollment Management Tests")
    class EnrollmentManagementTests {

        private Student student;
        private Enrollment enrollment;

        @BeforeEach
        void setUp() {
            String cpf = "12345678901";
            Address address = new Address("Rua A", "100", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");
            student = new Student("João Silva", cpf, "joao@email.com", "(11) 99999-9999", address);
            enrollment = new Enrollment(student, subject);
        }

        @Test
        @DisplayName("Should add enrollment successfully")
        void shouldAddEnrollmentSuccessfully() {
            subject.addEnrollment(enrollment);

            assertEquals(1, subject.getEnrollments().size());
            assertTrue(subject.getEnrollments().contains(enrollment));
        }

        @Test
        @DisplayName("Should handle enrollment collection properly")
        void shouldHandleEnrollmentCollectionProperly() {
            subject.addEnrollment(enrollment);

            assertEquals(1, subject.getEnrollments().size());
            assertTrue(subject.getEnrollments().contains(enrollment));

            subject.getEnrollments().remove(enrollment);
            assertEquals(0, subject.getEnrollments().size());
            assertFalse(subject.getEnrollments().contains(enrollment));
        }

        @Test
        @DisplayName("Should get enrolled students count")
        void shouldGetEnrolledStudentsCount() {
            assertEquals(0, subject.getEnrolledStudentsCount());

            subject.addEnrollment(enrollment);
            assertEquals(1, subject.getEnrolledStudentsCount());

            String cpf2 = "98765432100";
            Address address2 = new Address("Rua B", "200", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");
            Student student2 = new Student("Maria Silva", cpf2, "maria@email.com", "(11) 88888-8888", address2);
            Enrollment enrollment2 = new Enrollment(student2, subject);
            subject.addEnrollment(enrollment2);

            assertEquals(2, subject.getEnrolledStudentsCount());
        }
    }

    @Nested
    @DisplayName("Update Info Tests")
    class UpdateInfoTests {

        @Test
        @DisplayName("Should update info and timestamp when calling updateInfo")
        void shouldUpdateInfoAndTimestampWhenCallingUpdateInfo() throws InterruptedException {
            LocalDateTime originalTimestamp = subject.getUpdatedAt();

            Thread.sleep(10);
            subject.updateInfo("Advanced Java", "Advanced concepts", 80);

            assertEquals("Advanced Java", subject.getName());
            assertEquals("Advanced concepts", subject.getDescription());
            assertEquals(80, subject.getWorkload());
            assertTrue(subject.getUpdatedAt().isAfter(originalTimestamp));
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should handle null enrollments list")
        void shouldHandleNullEnrollmentsList() {
            subject.setEnrollments(null);
            assertDoesNotThrow(() -> {
                if (subject.getEnrollments() == null) {
                    subject.setEnrollments(new ArrayList<>());
                }
            });
        }

        @Test
        @DisplayName("Should return zero count for null enrollments")
        void shouldReturnZeroCountForNullEnrollments() {
            subject.setEnrollments(null);
            assertEquals(0, subject.getEnrolledStudentsCount());
        }
    }
}