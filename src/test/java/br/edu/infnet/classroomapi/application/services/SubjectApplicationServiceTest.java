package br.edu.infnet.classroomapi.application.services;

import br.edu.infnet.classroomapi.application.dto.request.CreateSubjectRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectSummaryDTO;
import br.edu.infnet.classroomapi.application.mappers.SubjectDTOMapper;
import br.edu.infnet.classroomapi.domain.entities.Professor;
import br.edu.infnet.classroomapi.domain.entities.Subject;
import br.edu.infnet.classroomapi.domain.repositories.ProfessorRepository;
import br.edu.infnet.classroomapi.domain.repositories.SubjectRepository;
import br.edu.infnet.classroomapi.infrastructure.persistence.repositories.EnrollmentJpaRepository;
import br.edu.infnet.classroomapi.infrastructure.security.services.SecurityContextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("SubjectApplicationService Tests")
class SubjectApplicationServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private SubjectDTOMapper subjectMapper;

    @Mock
    private EnrollmentJpaRepository enrollmentRepository;

    @Mock
    private SecurityContextService securityContextService;

    @InjectMocks
    private SubjectApplicationService subjectService;

    private CreateSubjectRequestDTO createSubjectRequest;
    private Subject subject;
    private Professor professor;
    private SubjectResponseDTO subjectResponseDTO;
    private SubjectSummaryDTO subjectSummaryDTO;

    @BeforeEach
    void setUp() {
        createSubjectRequest = new CreateSubjectRequestDTO(
                "Java Programming", "JAVA101", "Introduction to Java", 60
        );

        professor = new Professor("Prof. Silva", "prof@email.com", "password");
        professor.setId(1L);

        subject = new Subject("Java Programming", "JAVA101", "Introduction to Java", 60);
        subject.setProfessor(professor);
        subject.setId(1L);

        subjectResponseDTO = SubjectResponseDTO.builder()
                .id(1L)
                .name("Java Programming")
                .code("JAVA101")
                .description("Introduction to Java")
                .workload(60)
                .enrolledStudentsCount(0L)
                .build();

        subjectSummaryDTO = SubjectSummaryDTO.builder()
                .id(1L)
                .name("Java Programming")
                .code("JAVA101")
                .build();
    }

    @Nested
    @DisplayName("Create Subject Tests")
    class CreateSubjectTests {

        @Test
        @DisplayName("Should create subject successfully")
        void shouldCreateSubjectSuccessfully() {
            when(subjectRepository.existsByCode(anyString())).thenReturn(false);
            when(securityContextService.getCurrentProfessorId()).thenReturn(1L);
            when(professorRepository.findById(anyLong())).thenReturn(Optional.of(professor));
            when(subjectMapper.toDomain(createSubjectRequest)).thenReturn(subject);
            when(subjectRepository.save(any(Subject.class))).thenReturn(subject);
            when(subjectMapper.toResponseDTO(any(Subject.class), any(EnrollmentJpaRepository.class))).thenReturn(subjectResponseDTO);

            SubjectResponseDTO result = subjectService.createSubject(createSubjectRequest);

            assertNotNull(result);
            assertEquals(subjectResponseDTO.getId(), result.getId());
            assertEquals(subjectResponseDTO.getName(), result.getName());
            assertEquals(subjectResponseDTO.getCode(), result.getCode());

            verify(subjectRepository).existsByCode("JAVA101");
            verify(securityContextService).getCurrentProfessorId();
            verify(professorRepository).findById(1L);
            verify(subjectMapper).toDomain(createSubjectRequest);
            verify(subjectRepository).save(subject);
            verify(subjectMapper).toResponseDTO(subject, enrollmentRepository);
        }

        @Test
        @DisplayName("Should throw exception when subject code already exists")
        void shouldThrowExceptionWhenSubjectCodeAlreadyExists() {
            when(subjectRepository.existsByCode(anyString())).thenReturn(true);

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> subjectService.createSubject(createSubjectRequest)
            );

            assertEquals("Subject code already exists", exception.getMessage());
            verify(subjectRepository).existsByCode("JAVA101");
            verify(securityContextService, never()).getCurrentProfessorId();
            verify(subjectRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when professor not found")
        void shouldThrowExceptionWhenProfessorNotFound() {
            when(subjectRepository.existsByCode(anyString())).thenReturn(false);
            when(securityContextService.getCurrentProfessorId()).thenReturn(1L);
            when(professorRepository.findById(anyLong())).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> subjectService.createSubject(createSubjectRequest)
            );

            assertEquals("Professor not found", exception.getMessage());
            verify(professorRepository).findById(1L);
            verify(subjectRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Find Subject Tests")
    class FindSubjectTests {

        @Test
        @DisplayName("Should find subject by ID successfully")
        void shouldFindSubjectByIdSuccessfully() {
            when(subjectRepository.findById(anyLong())).thenReturn(Optional.of(subject));
            when(subjectMapper.toResponseDTO(any(Subject.class), any(EnrollmentJpaRepository.class))).thenReturn(subjectResponseDTO);

            SubjectResponseDTO result = subjectService.findById(1L);

            assertNotNull(result);
            assertEquals(subjectResponseDTO.getId(), result.getId());

            verify(subjectRepository).findById(1L);
            verify(subjectMapper).toResponseDTO(subject, enrollmentRepository);
        }

        @Test
        @DisplayName("Should throw exception when subject not found by ID")
        void shouldThrowExceptionWhenSubjectNotFoundById() {
            when(subjectRepository.findById(anyLong())).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> subjectService.findById(1L)
            );

            assertEquals("Subject not found with id: 1", exception.getMessage());
            verify(subjectRepository).findById(1L);
        }

        @Test
        @DisplayName("Should find subject by code successfully")
        void shouldFindSubjectByCodeSuccessfully() {
            when(subjectRepository.findByCode(anyString())).thenReturn(Optional.of(subject));
            when(subjectMapper.toResponseDTO(any(Subject.class), any(EnrollmentJpaRepository.class))).thenReturn(subjectResponseDTO);

            SubjectResponseDTO result = subjectService.findByCode("JAVA101");

            assertNotNull(result);
            assertEquals(subjectResponseDTO.getCode(), result.getCode());

            verify(subjectRepository).findByCode("JAVA101");
            verify(subjectMapper).toResponseDTO(subject, enrollmentRepository);
        }

        @Test
        @DisplayName("Should find all subjects successfully")
        void shouldFindAllSubjectsSuccessfully() {
            List<Subject> subjects = Arrays.asList(subject);
            List<SubjectResponseDTO> responseDTOs = Arrays.asList(subjectResponseDTO);

            when(subjectRepository.findAll()).thenReturn(subjects);
            when(subjectMapper.toResponseDTOList(any(List.class), any(EnrollmentJpaRepository.class))).thenReturn(responseDTOs);

            List<SubjectResponseDTO> result = subjectService.findAll();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(subjectResponseDTO.getId(), result.get(0).getId());

            verify(subjectRepository).findAll();
            verify(subjectMapper).toResponseDTOList(subjects, enrollmentRepository);
        }

        @Test
        @DisplayName("Should find all subjects summary successfully")
        void shouldFindAllSubjectsSummarySuccessfully() {
            List<Subject> subjects = Arrays.asList(subject);
            List<SubjectSummaryDTO> summaryDTOs = Arrays.asList(subjectSummaryDTO);

            when(subjectRepository.findAll()).thenReturn(subjects);
            when(subjectMapper.toSummaryDTOList(subjects)).thenReturn(summaryDTOs);

            List<SubjectSummaryDTO> result = subjectService.findAllSummary();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(subjectSummaryDTO.getId(), result.get(0).getId());

            verify(subjectRepository).findAll();
            verify(subjectMapper).toSummaryDTOList(subjects);
        }

        @Test
        @DisplayName("Should find subjects by name successfully")
        void shouldFindSubjectsByNameSuccessfully() {
            List<Subject> subjects = Arrays.asList(subject);
            List<SubjectResponseDTO> responseDTOs = Arrays.asList(subjectResponseDTO);

            when(subjectRepository.findByNameContainingIgnoreCase("Java")).thenReturn(subjects);
            when(subjectMapper.toResponseDTOList(any(List.class), any(EnrollmentJpaRepository.class))).thenReturn(responseDTOs);

            List<SubjectResponseDTO> result = subjectService.findByName("Java");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Java Programming", result.get(0).getName());

            verify(subjectRepository).findByNameContainingIgnoreCase("Java");
            verify(subjectMapper).toResponseDTOList(subjects, enrollmentRepository);
        }

        @Test
        @DisplayName("Should find subjects by current professor successfully")
        void shouldFindSubjectsByCurrentProfessorSuccessfully() {
            List<Subject> subjects = Arrays.asList(subject);
            List<SubjectResponseDTO> responseDTOs = Arrays.asList(subjectResponseDTO);

            when(securityContextService.getCurrentProfessorId()).thenReturn(1L);
            when(subjectRepository.findByProfessorId(1L)).thenReturn(subjects);
            when(subjectMapper.toResponseDTOList(any(List.class), any(EnrollmentJpaRepository.class))).thenReturn(responseDTOs);

            List<SubjectResponseDTO> result = subjectService.findByCurrentProfessor();

            assertNotNull(result);
            assertEquals(1, result.size());

            verify(securityContextService).getCurrentProfessorId();
            verify(subjectRepository).findByProfessorId(1L);
            verify(subjectMapper).toResponseDTOList(subjects, enrollmentRepository);
        }

        @Test
        @DisplayName("Should find subjects by professor ID successfully")
        void shouldFindSubjectsByProfessorIdSuccessfully() {
            List<Subject> subjects = Arrays.asList(subject);
            List<SubjectResponseDTO> responseDTOs = Arrays.asList(subjectResponseDTO);

            when(subjectRepository.findByProfessorId(1L)).thenReturn(subjects);
            when(subjectMapper.toResponseDTOList(any(List.class), any(EnrollmentJpaRepository.class))).thenReturn(responseDTOs);

            List<SubjectResponseDTO> result = subjectService.findByProfessorId(1L);

            assertNotNull(result);
            assertEquals(1, result.size());

            verify(subjectRepository).findByProfessorId(1L);
            verify(subjectMapper).toResponseDTOList(subjects, enrollmentRepository);
        }
    }

    @Nested
    @DisplayName("Update Subject Tests")
    class UpdateSubjectTests {

        @Test
        @DisplayName("Should update subject successfully")
        void shouldUpdateSubjectSuccessfully() {
            CreateSubjectRequestDTO updateRequest = new CreateSubjectRequestDTO(
                    "Advanced Java Programming", "JAVA101", "Advanced Java concepts", 80
            );

            Subject updatedSubject = new Subject("Advanced Java Programming", "JAVA101", "Advanced Java concepts", 80);
            updatedSubject.setProfessor(professor);
            updatedSubject.setId(1L);

            SubjectResponseDTO updatedResponseDTO = SubjectResponseDTO.builder()
                    .id(1L)
                    .name("Advanced Java Programming")
                    .code("JAVA101")
                    .description("Advanced Java concepts")
                    .workload(80)
                    .enrolledStudentsCount(0L)
                    .build();

            when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
            when(securityContextService.getCurrentProfessorId()).thenReturn(1L);
            when(subjectMapper.toDomain(updateRequest)).thenReturn(updatedSubject);
            when(subjectRepository.save(any(Subject.class))).thenReturn(updatedSubject);
            when(subjectMapper.toResponseDTO(any(Subject.class), any(EnrollmentJpaRepository.class))).thenReturn(updatedResponseDTO);

            SubjectResponseDTO result = subjectService.updateSubject(1L, updateRequest);

            assertNotNull(result);
            assertEquals("Advanced Java Programming", result.getName());
            assertEquals("Advanced Java concepts", result.getDescription());
            assertEquals(80, result.getWorkload());

            verify(subjectRepository).findById(1L);
            verify(securityContextService).getCurrentProfessorId();
            verify(subjectRepository).save(any(Subject.class));
        }

        @Test
        @DisplayName("Should throw exception when updating non-existent subject")
        void shouldThrowExceptionWhenUpdatingNonExistentSubject() {
            when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> subjectService.updateSubject(1L, createSubjectRequest)
            );

            assertEquals("Subject not found with id: 1", exception.getMessage());
            verify(subjectRepository).findById(1L);
            verify(subjectRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when professor doesn't own the subject")
        void shouldThrowExceptionWhenProfessorDoesNotOwnTheSubject() {
            when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
            when(securityContextService.getCurrentProfessorId()).thenReturn(2L);
            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> subjectService.updateSubject(1L, createSubjectRequest)
            );

            assertEquals("You can only update your own subjects", exception.getMessage());
            verify(subjectRepository).findById(1L);
            verify(securityContextService).getCurrentProfessorId();
            verify(subjectRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Delete Subject Tests")
    class DeleteSubjectTests {

        @Test
        @DisplayName("Should delete subject successfully")
        void shouldDeleteSubjectSuccessfully() {
            when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
            when(securityContextService.getCurrentProfessorId()).thenReturn(1L);

            assertDoesNotThrow(() -> subjectService.deleteById(1L));

            verify(subjectRepository).findById(1L);
            verify(securityContextService).getCurrentProfessorId();
            verify(subjectRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent subject")
        void shouldThrowExceptionWhenDeletingNonExistentSubject() {
            when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> subjectService.deleteById(1L)
            );

            assertEquals("Subject not found with id: 1", exception.getMessage());
            verify(subjectRepository).findById(1L);
            verify(subjectRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Should throw exception when professor doesn't own the subject")
        void shouldThrowExceptionWhenProfessorDoesNotOwnTheSubject() {
            when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
            when(securityContextService.getCurrentProfessorId()).thenReturn(2L);

            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> subjectService.deleteById(1L)
            );

            assertEquals("You can only delete your own subjects", exception.getMessage());
            verify(subjectRepository).findById(1L);
            verify(securityContextService).getCurrentProfessorId();
            verify(subjectRepository, never()).deleteById(anyLong());
        }
    }
}