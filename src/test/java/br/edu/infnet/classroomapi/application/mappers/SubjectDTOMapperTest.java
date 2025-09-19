package br.edu.infnet.classroomapi.application.mappers;

import br.edu.infnet.classroomapi.application.dto.request.CreateSubjectRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectSummaryDTO;
import br.edu.infnet.classroomapi.domain.entities.Professor;
import br.edu.infnet.classroomapi.domain.entities.Subject;
import br.edu.infnet.classroomapi.infrastructure.persistence.repositories.EnrollmentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SubjectDTOMapper Tests")
class SubjectDTOMapperTest {

    @Autowired
    private SubjectDTOMapper mapper;

    @MockitoBean
    private EnrollmentJpaRepository enrollmentRepository;

    private CreateSubjectRequestDTO createSubjectRequestDTO;
    private Subject subject;
    private Professor professor;

    @BeforeEach
    void setUp() {

        createSubjectRequestDTO = new CreateSubjectRequestDTO(
            "Java Programming", "JAVA101", "Introduction to Java", 60
        );

        professor = new Professor("Prof. Silva", "prof@email.com", "password");
        professor.setId(1L);

        subject = new Subject("Java Programming", "JAVA101", "Introduction to Java", 60);
        subject.setProfessor(professor);
        subject.setId(1L);
        subject.setCreatedAt(LocalDateTime.now());
        subject.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("Domain Mapping Tests")
    class DomainMappingTests {

        @Test
        @DisplayName("Should map CreateSubjectRequestDTO to domain Subject successfully")
        void shouldMapCreateSubjectRequestDTOToDomainSuccessfully() {
            Subject result = mapper.toDomain(createSubjectRequestDTO);

            assertNotNull(result);
            assertEquals(createSubjectRequestDTO.getName(), result.getName());
            assertEquals(createSubjectRequestDTO.getCode(), result.getCode());
            assertEquals(createSubjectRequestDTO.getDescription(), result.getDescription());
            assertEquals(createSubjectRequestDTO.getWorkload(), result.getWorkload());
            assertNull(result.getId());
            assertNull(result.getProfessor());
            assertNull(result.getCreatedAt());
            assertNull(result.getUpdatedAt());
            assertNull(result.getEnrollments());
        }

        @Test
        @DisplayName("Should handle null CreateSubjectRequestDTO")
        void shouldHandleNullCreateSubjectRequestDTO() {
            Subject result = mapper.toDomain(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Should map CreateSubjectRequestDTO with null fields")
        void shouldMapCreateSubjectRequestDTOWithNullFields() {
            CreateSubjectRequestDTO requestWithNulls = new CreateSubjectRequestDTO(
                "Java Programming", null, null, 60
            );

            Subject result = mapper.toDomain(requestWithNulls);

            assertNotNull(result);
            assertEquals("Java Programming", result.getName());
            assertNull(result.getCode());
            assertNull(result.getDescription());
            assertEquals(60, result.getWorkload());
        }
    }

    @Nested
    @DisplayName("Response DTO Mapping Tests")
    class ResponseDTOMappingTests {

        @Test
        @DisplayName("Should map domain Subject to SubjectResponseDTO successfully")
        void shouldMapDomainSubjectToResponseDTOSuccessfully() {
            when(enrollmentRepository.countBySubjectId(1L)).thenReturn(5L);

            SubjectResponseDTO result = mapper.toResponseDTO(subject, enrollmentRepository);

            assertNotNull(result);
            assertEquals(subject.getId(), result.getId());
            assertEquals(subject.getName(), result.getName());
            assertEquals(subject.getCode(), result.getCode());
            assertEquals(subject.getDescription(), result.getDescription());
            assertEquals(subject.getWorkload(), result.getWorkload());
            assertEquals(5L, result.getEnrolledStudentsCount());
            assertNotNull(result.getProfessor());
            assertEquals(professor.getName(), result.getProfessor().getName());
        }

        @Test
        @DisplayName("Should handle null domain Subject")
        void shouldHandleNullDomainSubject() {
            SubjectResponseDTO result = mapper.toResponseDTO(null, enrollmentRepository);
            assertNull(result);
        }

        @Test
        @DisplayName("Should handle Subject with null ID")
        void shouldHandleSubjectWithNullId() {
            subject.setId(null);

            SubjectResponseDTO result = mapper.toResponseDTO(subject, enrollmentRepository);

            assertNotNull(result);
            assertEquals(0L, result.getEnrolledStudentsCount());
        }

        @Test
        @DisplayName("Should handle Subject with null professor")
        void shouldHandleSubjectWithNullProfessor() {
            subject.setProfessor(null);

            SubjectResponseDTO result = mapper.toResponseDTO(subject, enrollmentRepository);

            assertNotNull(result);
            assertEquals(subject.getName(), result.getName());
            assertNull(result.getProfessor());
        }
    }

    @Nested
    @DisplayName("Summary DTO Mapping Tests")
    class SummaryDTOMappingTests {

        @Test
        @DisplayName("Should map domain Subject to SubjectSummaryDTO successfully")
        void shouldMapDomainSubjectToSummaryDTOSuccessfully() {
            SubjectSummaryDTO result = mapper.toSummaryDTO(subject);

            assertNotNull(result);
            assertEquals(subject.getId(), result.getId());
            assertEquals(subject.getName(), result.getName());
            assertEquals(subject.getCode(), result.getCode());
        }

        @Test
        @DisplayName("Should handle null domain Subject for summary")
        void shouldHandleNullDomainSubjectForSummary() {
            SubjectSummaryDTO result = mapper.toSummaryDTO(null);
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("List Mapping Tests")
    class ListMappingTests {

        @Test
        @DisplayName("Should map list of Subjects to list of SubjectResponseDTO successfully")
        void shouldMapListOfSubjectsToResponseDTOListSuccessfully() {
            Subject subject2 = new Subject("Python Programming", "PY101", "Introduction to Python", 45);
            subject2.setId(2L);
            List<Subject> subjects = Arrays.asList(subject, subject2);

            when(enrollmentRepository.countBySubjectId(1L)).thenReturn(3L);
            when(enrollmentRepository.countBySubjectId(2L)).thenReturn(7L);

            List<SubjectResponseDTO> result = mapper.toResponseDTOList(subjects, enrollmentRepository);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(subject.getName(), result.get(0).getName());
            assertEquals(subject2.getName(), result.get(1).getName());
            assertEquals(3L, result.get(0).getEnrolledStudentsCount());
            assertEquals(7L, result.get(1).getEnrolledStudentsCount());
        }

        @Test
        @DisplayName("Should map list of Subjects to list of SubjectSummaryDTO successfully")
        void shouldMapListOfSubjectsToSummaryDTOListSuccessfully() {
            Subject subject2 = new Subject("Python Programming", "PY101", "Introduction to Python", 45);
            subject2.setId(2L);
            List<Subject> subjects = Arrays.asList(subject, subject2);

            List<SubjectSummaryDTO> result = mapper.toSummaryDTOList(subjects);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(subject.getName(), result.get(0).getName());
            assertEquals(subject2.getName(), result.get(1).getName());
        }

        @Test
        @DisplayName("Should handle null list")
        void shouldHandleNullList() {
            List<SubjectResponseDTO> responseResult = mapper.toResponseDTOList(null, enrollmentRepository);
            List<SubjectSummaryDTO> summaryResult = mapper.toSummaryDTOList(null);

            assertNull(responseResult);
            assertNull(summaryResult);
        }

        @Test
        @DisplayName("Should handle empty list")
        void shouldHandleEmptyList() {
            List<Subject> emptyList = Arrays.asList();

            List<SubjectResponseDTO> responseResult = mapper.toResponseDTOList(emptyList, enrollmentRepository);
            List<SubjectSummaryDTO> summaryResult = mapper.toSummaryDTOList(emptyList);

            assertNotNull(responseResult);
            assertNotNull(summaryResult);
            assertTrue(responseResult.isEmpty());
            assertTrue(summaryResult.isEmpty());
        }
    }

    @Nested
    @DisplayName("Enrolled Students Count Tests")
    class EnrolledStudentsCountTests {

        @Test
        @DisplayName("Should return enrollment count when subject has ID")
        void shouldReturnEnrollmentCountWhenSubjectHasId() {
            when(enrollmentRepository.countBySubjectId(1L)).thenReturn(10L);

            Long result = mapper.getEnrolledStudentsCount(subject, enrollmentRepository);

            assertEquals(10L, result);
        }

        @Test
        @DisplayName("Should return 0 when subject has null ID")
        void shouldReturnZeroWhenSubjectHasNullId() {
            subject.setId(null);

            Long result = mapper.getEnrolledStudentsCount(subject, enrollmentRepository);

            assertEquals(0L, result);
        }
    }
}