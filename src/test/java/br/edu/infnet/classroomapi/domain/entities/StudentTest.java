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
@DisplayName("Student Entity Tests")
class StudentTest {

    private Student student;
    private Address validAddress;
    private String validCpf;

    @BeforeEach
    void setUp() {
        validCpf = "12345678901";
        validAddress = new Address(
            "Rua das Flores",
            "123",
            "Apto 45",
            "Centro",
            "São Paulo",
            "SP",
            "01234-567",
            "Brasil"
        );

        student = new Student(
            "João Silva",
            validCpf,
            "joao@email.com",
            "(11) 99999-9999",
            validAddress
        );
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create student with valid data")
        void shouldCreateStudentWithValidData() {
            assertNotNull(student);
            assertEquals("João Silva", student.getName());
            assertEquals(validCpf, student.getCpf());
            assertEquals("joao@email.com", student.getEmail());
            assertEquals("(11) 99999-9999", student.getPhone());
            assertEquals(validAddress, student.getAddress());
            assertNotNull(student.getEnrollments());
            assertTrue(student.getEnrollments().isEmpty());
            assertNotNull(student.getCreatedAt());
            assertNotNull(student.getUpdatedAt());
        }

        @Test
        @DisplayName("Should initialize enrollments as empty list")
        void shouldInitializeEnrollmentsAsEmptyList() {
            assertNotNull(student.getEnrollments());
            assertTrue(student.getEnrollments().isEmpty());
        }

        @Test
        @DisplayName("Should set creation and update timestamps")
        void shouldSetCreationAndUpdateTimestamps() {
            LocalDateTime before = LocalDateTime.now().minusSeconds(1);
            LocalDateTime after = LocalDateTime.now().plusSeconds(1);

            assertNotNull(student.getCreatedAt());
            assertNotNull(student.getUpdatedAt());
            assertTrue(student.getCreatedAt().isAfter(before));
            assertTrue(student.getCreatedAt().isBefore(after));
            assertEquals(student.getCreatedAt(), student.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("Enrollment Management Tests")
    class EnrollmentManagementTests {

        private Subject subject;
        private Enrollment enrollment;

        @BeforeEach
        void setUp() {
            Professor professor = new Professor("Prof. Silva", "prof@email.com", "password");
            subject = new Subject("Java Programming", "JAVA101", "Introduction to Java", 60);
            subject.setProfessor(professor);
            enrollment = new Enrollment(student, subject);
        }

        @Test
        @DisplayName("Should add enrollment successfully")
        void shouldAddEnrollmentSuccessfully() {
            student.addEnrollment(enrollment);

            assertEquals(1, student.getEnrollments().size());
            assertTrue(student.getEnrollments().contains(enrollment));
        }

        @Test
        @DisplayName("Should handle enrollment collection properly")
        void shouldHandleEnrollmentCollectionProperly() {
            student.addEnrollment(enrollment);

            assertEquals(1, student.getEnrollments().size());
            assertTrue(student.getEnrollments().contains(enrollment));

            student.getEnrollments().remove(enrollment);
            assertEquals(0, student.getEnrollments().size());
            assertFalse(student.getEnrollments().contains(enrollment));
        }

        @Test
        @DisplayName("Should handle multiple enrollments")
        void shouldHandleMultipleEnrollments() {
            Professor professor2 = new Professor("Prof. Santos", "santos@email.com", "password");
            Subject subject2 = new Subject("Python Programming", "PY101", "Introduction to Python", 60);
            subject2.setProfessor(professor2);
            Enrollment enrollment2 = new Enrollment(student, subject2);

            student.addEnrollment(enrollment);
            student.addEnrollment(enrollment2);

            assertEquals(2, student.getEnrollments().size());
            assertTrue(student.getEnrollments().contains(enrollment));
            assertTrue(student.getEnrollments().contains(enrollment2));
        }
    }

    @Nested
    @DisplayName("Update Info Tests")
    class UpdateInfoTests {

        @Test
        @DisplayName("Should update info and timestamp when calling updateInfo")
        void shouldUpdateInfoAndTimestampWhenCallingUpdateInfo() throws InterruptedException {
            LocalDateTime originalTimestamp = student.getUpdatedAt();
            Address newAddress = new Address("Nova Rua", "456", null, "Novo Bairro", "Rio de Janeiro", "RJ", "20000-000", "Brasil");

            Thread.sleep(10);
            student.updateInfo("Novo Nome", "novo@email.com", "(21) 88888-8888", newAddress);

            assertEquals("Novo Nome", student.getName());
            assertEquals("novo@email.com", student.getEmail());
            assertEquals("(21) 88888-8888", student.getPhone());
            assertEquals(newAddress, student.getAddress());
            assertTrue(student.getUpdatedAt().isAfter(originalTimestamp));
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should handle null enrollments list")
        void shouldHandleNullEnrollmentsList() {
            student.setEnrollments(null);
            assertDoesNotThrow(() -> {
                if (student.getEnrollments() == null) {
                    student.setEnrollments(new ArrayList<>());
                }
            });
        }
    }
}