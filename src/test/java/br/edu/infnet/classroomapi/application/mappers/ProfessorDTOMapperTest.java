package br.edu.infnet.classroomapi.application.mappers;

import br.edu.infnet.classroomapi.application.dto.request.CreateProfessorRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.ProfessorResponseDTO;
import br.edu.infnet.classroomapi.domain.entities.Professor;
import br.edu.infnet.classroomapi.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProfessorDTOMapper Tests")
class ProfessorDTOMapperTest {

    private ProfessorDTOMapper mapper;
    private CreateProfessorRequestDTO createProfessorRequestDTO;
    private Professor professor;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ProfessorDTOMapper.class);

        createProfessorRequestDTO = new CreateProfessorRequestDTO(
            "Prof. Silva", "prof@email.com", "password123"
        );

        professor = new Professor("Prof. Silva", "prof@email.com", "password123");
        professor.setId(1L);
        professor.setRole(UserRole.PROFESSOR);
        professor.setCreatedAt(LocalDateTime.now());
        professor.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("Domain Mapping Tests")
    class DomainMappingTests {

        @Test
        @DisplayName("Should map CreateProfessorRequestDTO to domain Professor successfully")
        void shouldMapCreateProfessorRequestDTOToDomainSuccessfully() {
            Professor result = mapper.toDomain(createProfessorRequestDTO);

            assertNotNull(result);
            assertEquals(createProfessorRequestDTO.getName(), result.getName());
            assertEquals(createProfessorRequestDTO.getEmail(), result.getEmail());
            assertEquals(createProfessorRequestDTO.getPassword(), result.getPassword());
            assertNull(result.getId());
            assertNull(result.getRole());
            assertNull(result.getCreatedAt());
            assertNull(result.getUpdatedAt());
            assertNull(result.getSubjects());
        }

        @Test
        @DisplayName("Should handle null CreateProfessorRequestDTO")
        void shouldHandleNullCreateProfessorRequestDTO() {
            Professor result = mapper.toDomain(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Should map CreateProfessorRequestDTO with null fields")
        void shouldMapCreateProfessorRequestDTOWithNullFields() {
            CreateProfessorRequestDTO requestWithNulls = new CreateProfessorRequestDTO(
                "Prof. Silva", null, "password123"
            );

            Professor result = mapper.toDomain(requestWithNulls);

            assertNotNull(result);
            assertEquals("Prof. Silva", result.getName());
            assertNull(result.getEmail());
            assertEquals("password123", result.getPassword());
        }
    }

    @Nested
    @DisplayName("Response DTO Mapping Tests")
    class ResponseDTOMappingTests {

        @Test
        @DisplayName("Should map domain Professor to ProfessorResponseDTO successfully")
        void shouldMapDomainProfessorToResponseDTOSuccessfully() {
            ProfessorResponseDTO result = mapper.toResponseDTO(professor);

            assertNotNull(result);
            assertEquals(professor.getId(), result.getId());
            assertEquals(professor.getName(), result.getName());
            assertEquals(professor.getEmail(), result.getEmail());
            assertEquals(professor.getRole(), result.getRole());
            assertEquals(professor.getCreatedAt(), result.getCreatedAt());
            assertEquals(professor.getUpdatedAt(), result.getUpdatedAt());
        }

        @Test
        @DisplayName("Should handle null domain Professor")
        void shouldHandleNullDomainProfessor() {
            ProfessorResponseDTO result = mapper.toResponseDTO(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Should map domain Professor with null fields")
        void shouldMapDomainProfessorWithNullFields() {
            professor.setRole(null);
            professor.setCreatedAt(null);
            professor.setUpdatedAt(null);

            ProfessorResponseDTO result = mapper.toResponseDTO(professor);

            assertNotNull(result);
            assertEquals(professor.getName(), result.getName());
            assertNull(result.getRole());
            assertNull(result.getCreatedAt());
            assertNull(result.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("List Mapping Tests")
    class ListMappingTests {

        @Test
        @DisplayName("Should map list of Professors to list of ProfessorResponseDTO successfully")
        void shouldMapListOfProfessorsToResponseDTOListSuccessfully() {
            Professor professor2 = new Professor("Prof. Santos", "santos@email.com", "password456");
            professor2.setId(2L);
            professor2.setRole(UserRole.PROFESSOR);
            List<Professor> professors = Arrays.asList(professor, professor2);

            List<ProfessorResponseDTO> result = mapper.toResponseDTOList(professors);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(professor.getName(), result.get(0).getName());
            assertEquals(professor2.getName(), result.get(1).getName());
            assertEquals(professor.getEmail(), result.get(0).getEmail());
            assertEquals(professor2.getEmail(), result.get(1).getEmail());
        }

        @Test
        @DisplayName("Should handle null list")
        void shouldHandleNullList() {
            List<ProfessorResponseDTO> result = mapper.toResponseDTOList(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Should handle empty list")
        void shouldHandleEmptyList() {
            List<Professor> emptyList = Arrays.asList();

            List<ProfessorResponseDTO> result = mapper.toResponseDTOList(emptyList);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}