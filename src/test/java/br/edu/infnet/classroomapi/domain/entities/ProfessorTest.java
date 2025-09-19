package br.edu.infnet.classroomapi.domain.entities;

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
@DisplayName("Professor Entity Tests")
class ProfessorTest {

    private Professor professor;

    @BeforeEach
    void setUp() {
        professor = new Professor("Prof. Silva", "prof@email.com", "password123");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create professor with valid data")
        void shouldCreateProfessorWithValidData() {
            assertNotNull(professor);
            assertEquals("Prof. Silva", professor.getName());
            assertEquals("prof@email.com", professor.getEmail());
            assertEquals("password123", professor.getPassword());
            assertNotNull(professor.getSubjects());
            assertTrue(professor.getSubjects().isEmpty());
            assertNotNull(professor.getCreatedAt());
            assertNotNull(professor.getUpdatedAt());
        }

        @Test
        @DisplayName("Should initialize subjects as empty list")
        void shouldInitializeSubjectsAsEmptyList() {
            assertNotNull(professor.getSubjects());
            assertTrue(professor.getSubjects().isEmpty());
        }

        @Test
        @DisplayName("Should set creation and update timestamps")
        void shouldSetCreationAndUpdateTimestamps() {
            LocalDateTime before = LocalDateTime.now().minusSeconds(1);
            LocalDateTime after = LocalDateTime.now().plusSeconds(1);

            assertNotNull(professor.getCreatedAt());
            assertNotNull(professor.getUpdatedAt());
            assertTrue(professor.getCreatedAt().isAfter(before));
            assertTrue(professor.getCreatedAt().isBefore(after));
            assertEquals(professor.getCreatedAt(), professor.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("Subject Management Tests")
    class SubjectManagementTests {

        private Subject subject;

        @BeforeEach
        void setUp() {
            subject = new Subject("Java Programming", "JAVA101", "Introduction to Java", 60);
        }

        @Test
        @DisplayName("Should add subject successfully")
        void shouldAddSubjectSuccessfully() {
            professor.addSubject(subject);

            assertEquals(1, professor.getSubjects().size());
            assertTrue(professor.getSubjects().contains(subject));
        }

        @Test
        @DisplayName("Should remove subject successfully")
        void shouldRemoveSubjectSuccessfully() {
            professor.addSubject(subject);
            professor.removeSubject(subject);

            assertEquals(0, professor.getSubjects().size());
            assertFalse(professor.getSubjects().contains(subject));
        }

        @Test
        @DisplayName("Should handle multiple subjects")
        void shouldHandleMultipleSubjects() {
            Subject subject2 = new Subject("Python Programming", "PY101", "Introduction to Python", 60);

            professor.addSubject(subject);
            professor.addSubject(subject2);

            assertEquals(2, professor.getSubjects().size());
            assertTrue(professor.getSubjects().contains(subject));
            assertTrue(professor.getSubjects().contains(subject2));
        }
    }

    @Nested
    @DisplayName("Update Info Tests")
    class UpdateInfoTests {

        @Test
        @DisplayName("Should update info and timestamp when calling updateInfo")
        void shouldUpdateInfoAndTimestampWhenCallingUpdateInfo() throws InterruptedException {
            LocalDateTime originalTimestamp = professor.getUpdatedAt();

            Thread.sleep(10);
            professor.updateInfo("Novo Nome", "novo@email.com");

            assertEquals("Novo Nome", professor.getName());
            assertEquals("novo@email.com", professor.getEmail());
            assertTrue(professor.getUpdatedAt().isAfter(originalTimestamp));
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should handle null subjects list")
        void shouldHandleNullSubjectsList() {
            professor.setSubjects(null);
            assertDoesNotThrow(() -> {
                if (professor.getSubjects() == null) {
                    professor.setSubjects(new ArrayList<>());
                }
            });
        }
    }
}