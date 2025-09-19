package br.edu.infnet.classroomapi.application.services;

import br.edu.infnet.classroomapi.application.dto.request.CreateProfessorRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.ProfessorResponseDTO;
import br.edu.infnet.classroomapi.application.mappers.ProfessorDTOMapper;
import br.edu.infnet.classroomapi.domain.entities.Professor;
import br.edu.infnet.classroomapi.domain.enums.UserRole;
import br.edu.infnet.classroomapi.domain.repositories.ProfessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
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
@DisplayName("ProfessorApplicationService Tests")
class ProfessorApplicationServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private ProfessorDTOMapper professorMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfessorApplicationService professorService;

    private CreateProfessorRequestDTO createProfessorRequest;
    private Professor professor;
    private ProfessorResponseDTO professorResponseDTO;

    @BeforeEach
    void setUp() {
        createProfessorRequest = new CreateProfessorRequestDTO(
                "Prof. João Silva",
                "professor@email.com",
                "password123"
        );

        professor = new Professor();
        professor.setId(1L);
        professor.setName("Prof. João Silva");
        professor.setEmail("professor@email.com");
        professor.setPassword("encodedPassword123");
        professor.setRole(UserRole.PROFESSOR);
        professor.setCreatedAt(LocalDateTime.now());
        professor.setUpdatedAt(LocalDateTime.now());

        professorResponseDTO = ProfessorResponseDTO.builder()
                .id(1L)
                .name("Prof. João Silva")
                .email("professor@email.com")
                .role(UserRole.PROFESSOR)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("Create Professor Tests")
    class CreateProfessorTests {

        @Test
        @DisplayName("Should create professor successfully")
        void shouldCreateProfessorSuccessfully() {
            when(professorRepository.existsByEmail(anyString())).thenReturn(false);
            when(professorMapper.toDomain(any(CreateProfessorRequestDTO.class))).thenReturn(professor);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword123");
            when(professorRepository.save(any(Professor.class))).thenReturn(professor);
            when(professorMapper.toResponseDTO(any(Professor.class))).thenReturn(professorResponseDTO);

            ProfessorResponseDTO result = professorService.createProfessor(createProfessorRequest);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Prof. João Silva", result.getName());
            assertEquals("professor@email.com", result.getEmail());
            assertEquals(UserRole.PROFESSOR, result.getRole());

            verify(professorRepository).existsByEmail("professor@email.com");
            verify(professorMapper).toDomain(createProfessorRequest);
            verify(passwordEncoder).encode("password123");
            verify(professorRepository).save(any(Professor.class));
            verify(professorMapper).toResponseDTO(professor);
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            when(professorRepository.existsByEmail(anyString())).thenReturn(true);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> professorService.createProfessor(createProfessorRequest));

            assertEquals("Email already exists", exception.getMessage());

            verify(professorRepository).existsByEmail("professor@email.com");
            verifyNoInteractions(professorMapper, passwordEncoder);
            verify(professorRepository, never()).save(any(Professor.class));
        }

        @Test
        @DisplayName("Should encode password when creating professor")
        void shouldEncodePasswordWhenCreatingProfessor() {
            when(professorRepository.existsByEmail(anyString())).thenReturn(false);
            when(professorMapper.toDomain(any(CreateProfessorRequestDTO.class))).thenReturn(professor);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword123");
            when(professorRepository.save(any(Professor.class))).thenReturn(professor);
            when(professorMapper.toResponseDTO(any(Professor.class))).thenReturn(professorResponseDTO);

            professorService.createProfessor(createProfessorRequest);

            verify(passwordEncoder).encode("password123");
            verify(professorRepository).save(argThat(prof -> 
                "encodedPassword123".equals(prof.getPassword())
            ));
        }
    }

    @Nested
    @DisplayName("Find Professor Tests")
    class FindProfessorTests {

        @Test
        @DisplayName("Should find professor by ID successfully")
        void shouldFindProfessorByIdSuccessfully() {
            when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
            when(professorMapper.toResponseDTO(any(Professor.class))).thenReturn(professorResponseDTO);

            ProfessorResponseDTO result = professorService.findById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Prof. João Silva", result.getName());
            assertEquals("professor@email.com", result.getEmail());

            verify(professorRepository).findById(1L);
            verify(professorMapper).toResponseDTO(professor);
        }

        @Test
        @DisplayName("Should throw exception when professor not found by ID")
        void shouldThrowExceptionWhenProfessorNotFoundById() {
            when(professorRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> professorService.findById(1L));

            assertEquals("Professor not found with id: 1", exception.getMessage());

            verify(professorRepository).findById(1L);
            verifyNoInteractions(professorMapper);
        }

        @Test
        @DisplayName("Should find professor by email successfully")
        void shouldFindProfessorByEmailSuccessfully() {
            when(professorRepository.findByEmail("professor@email.com")).thenReturn(Optional.of(professor));
            when(professorMapper.toResponseDTO(any(Professor.class))).thenReturn(professorResponseDTO);

            ProfessorResponseDTO result = professorService.findByEmail("professor@email.com");

            assertNotNull(result);
            assertEquals("professor@email.com", result.getEmail());
            assertEquals("Prof. João Silva", result.getName());

            verify(professorRepository).findByEmail("professor@email.com");
            verify(professorMapper).toResponseDTO(professor);
        }

        @Test
        @DisplayName("Should throw exception when professor not found by email")
        void shouldThrowExceptionWhenProfessorNotFoundByEmail() {
            when(professorRepository.findByEmail("nonexistent@email.com")).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> professorService.findByEmail("nonexistent@email.com"));

            assertEquals("Professor not found with email: nonexistent@email.com", exception.getMessage());

            verify(professorRepository).findByEmail("nonexistent@email.com");
            verifyNoInteractions(professorMapper);
        }

        @Test
        @DisplayName("Should find all professors successfully")
        void shouldFindAllProfessorsSuccessfully() {
            Professor professor2 = new Professor();
            professor2.setId(2L);
            professor2.setName("Prof. Maria Santos");
            professor2.setEmail("maria@email.com");
            professor2.setPassword("encodedPassword");
            professor2.setRole(UserRole.PROFESSOR);

            ProfessorResponseDTO professorResponseDTO2 = ProfessorResponseDTO.builder()
                    .id(2L)
                    .name("Prof. Maria Santos")
                    .email("maria@email.com")
                    .role(UserRole.PROFESSOR)
                    .build();

            List<Professor> professors = Arrays.asList(professor, professor2);
            List<ProfessorResponseDTO> responseDTOs = Arrays.asList(professorResponseDTO, professorResponseDTO2);

            when(professorRepository.findAll()).thenReturn(professors);
            when(professorMapper.toResponseDTOList(professors)).thenReturn(responseDTOs);

            List<ProfessorResponseDTO> result = professorService.findAll();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Prof. João Silva", result.get(0).getName());
            assertEquals("Prof. Maria Santos", result.get(1).getName());

            verify(professorRepository).findAll();
            verify(professorMapper).toResponseDTOList(professors);
        }

        @Test
        @DisplayName("Should find professors by name successfully")
        void shouldFindProfessorsByNameSuccessfully() {
            List<Professor> professors = Arrays.asList(professor);
            List<ProfessorResponseDTO> responseDTOs = Arrays.asList(professorResponseDTO);

            when(professorRepository.findByNameContainingIgnoreCase("João")).thenReturn(professors);
            when(professorMapper.toResponseDTOList(professors)).thenReturn(responseDTOs);

            List<ProfessorResponseDTO> result = professorService.findByName("João");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Prof. João Silva", result.get(0).getName());

            verify(professorRepository).findByNameContainingIgnoreCase("João");
            verify(professorMapper).toResponseDTOList(professors);
        }

        @Test
        @DisplayName("Should return empty list when no professors found by name")
        void shouldReturnEmptyListWhenNoProfessorsFoundByName() {
            when(professorRepository.findByNameContainingIgnoreCase("NonExistent")).thenReturn(Arrays.asList());
            when(professorMapper.toResponseDTOList(Arrays.asList())).thenReturn(Arrays.asList());

            List<ProfessorResponseDTO> result = professorService.findByName("NonExistent");

            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(professorRepository).findByNameContainingIgnoreCase("NonExistent");
            verify(professorMapper).toResponseDTOList(Arrays.asList());
        }
    }

    @Nested
    @DisplayName("Delete Professor Tests")
    class DeleteProfessorTests {

        @Test
        @DisplayName("Should delete professor successfully")
        void shouldDeleteProfessorSuccessfully() {
            when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));

            assertDoesNotThrow(() -> professorService.deleteById(1L));

            verify(professorRepository).findById(1L);
            verify(professorRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw exception when professor not found for deletion")
        void shouldThrowExceptionWhenProfessorNotFoundForDeletion() {
            when(professorRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> professorService.deleteById(1L));

            assertEquals("Professor not found with id: 1", exception.getMessage());

            verify(professorRepository).findById(1L);
            verify(professorRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Should verify professor exists before deletion")
        void shouldVerifyProfessorExistsBeforeDeletion() {
            when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));

            professorService.deleteById(1L);

            verify(professorRepository).findById(1L);
            verify(professorRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should handle multiple professors with same name search")
        void shouldHandleMultipleProfessorsWithSameNameSearch() {
            Professor professor1 = new Professor();
            professor1.setId(1L);
            professor1.setName("João Silva");
            professor1.setEmail("joao1@email.com");

            Professor professor2 = new Professor();
            professor2.setId(2L);
            professor2.setName("João Santos");
            professor2.setEmail("joao2@email.com");

            ProfessorResponseDTO response1 = ProfessorResponseDTO.builder()
                    .id(1L)
                    .name("João Silva")
                    .email("joao1@email.com")
                    .build();

            ProfessorResponseDTO response2 = ProfessorResponseDTO.builder()
                    .id(2L)
                    .name("João Santos")
                    .email("joao2@email.com")
                    .build();

            List<Professor> professors = Arrays.asList(professor1, professor2);
            List<ProfessorResponseDTO> responses = Arrays.asList(response1, response2);

            when(professorRepository.findByNameContainingIgnoreCase("João")).thenReturn(professors);
            when(professorMapper.toResponseDTOList(professors)).thenReturn(responses);

            List<ProfessorResponseDTO> result = professorService.findByName("João");

            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(prof -> prof.getName().contains("João")));

            verify(professorRepository).findByNameContainingIgnoreCase("João");
        }

        @Test
        @DisplayName("Should maintain data integrity during professor creation")
        void shouldMaintainDataIntegrityDuringProfessorCreation() {
            when(professorRepository.existsByEmail(anyString())).thenReturn(false);
            when(professorMapper.toDomain(any(CreateProfessorRequestDTO.class))).thenReturn(professor);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword123");
            when(professorRepository.save(any(Professor.class))).thenReturn(professor);
            when(professorMapper.toResponseDTO(any(Professor.class))).thenReturn(professorResponseDTO);

            professorService.createProfessor(createProfessorRequest);

            verify(passwordEncoder).encode("password123");

            verify(professorRepository).save(argThat(prof -> 
                !"password123".equals(prof.getPassword()) &&
                "encodedPassword123".equals(prof.getPassword())
            ));
        }
    }
}
