package br.edu.infnet.classroomapi.application.mappers;

import br.edu.infnet.classroomapi.application.dto.request.AddressRequestDTO;
import br.edu.infnet.classroomapi.application.dto.request.CreateStudentRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentSummaryDTO;
import br.edu.infnet.classroomapi.domain.entities.Address;
import br.edu.infnet.classroomapi.domain.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("StudentDTOMapper Tests")
class StudentDTOMapperTest {

    @Autowired
    private StudentDTOMapper mapper;
    private CreateStudentRequestDTO createStudentRequestDTO;
    private Student student;
    private AddressRequestDTO addressRequest;
    private Address address;

    @BeforeEach
    void setUp() {

        addressRequest = new AddressRequestDTO(
            "Rua das Flores", "123", "Apto 45", "Centro",
            "São Paulo", "SP", "01234-567", "Brasil"
        );

        createStudentRequestDTO = new CreateStudentRequestDTO(
            "João Silva", "12345678901", "joao@email.com", "(11) 99999-9999", addressRequest
        );

        address = new Address(
            "Rua das Flores", "123", "Apto 45", "Centro",
            "São Paulo", "SP", "01234-567", "Brasil"
        );

        student = new Student("João Silva", "12345678901", "joao@email.com", "(11) 99999-9999", address);
        student.setId(1L);
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("Domain Mapping Tests")
    class DomainMappingTests {

        @Test
        @DisplayName("Should map CreateStudentRequestDTO to domain Student successfully")
        void shouldMapCreateStudentRequestDTOToDomainSuccessfully() {
            Student result = mapper.toDomain(createStudentRequestDTO);

            assertNotNull(result);
            assertEquals(createStudentRequestDTO.getName(), result.getName());
            assertEquals(createStudentRequestDTO.getCpf(), result.getCpf());
            assertEquals(createStudentRequestDTO.getEmail(), result.getEmail());
            assertEquals(createStudentRequestDTO.getPhone(), result.getPhone());
            assertNotNull(result.getAddress());
            assertEquals(createStudentRequestDTO.getAddress().getStreet(), result.getAddress().getStreet());
            assertNull(result.getId());
            assertNull(result.getCreatedAt());
            assertNull(result.getUpdatedAt());
            assertNull(result.getEnrollments());
        }

        @Test
        @DisplayName("Should handle null CreateStudentRequestDTO")
        void shouldHandleNullCreateStudentRequestDTO() {
            Student result = mapper.toDomain(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Should map CreateStudentRequestDTO with null address")
        void shouldMapCreateStudentRequestDTOWithNullAddress() {
            CreateStudentRequestDTO requestWithNullAddress = new CreateStudentRequestDTO(
                "João Silva", "12345678901", "joao@email.com", "(11) 99999-9999", null
            );

            Student result = mapper.toDomain(requestWithNullAddress);

            assertNotNull(result);
            assertEquals("João Silva", result.getName());
            assertNull(result.getAddress());
        }
    }

    @Nested
    @DisplayName("Response DTO Mapping Tests")
    class ResponseDTOMappingTests {

        @Test
        @DisplayName("Should map domain Student to StudentResponseDTO successfully")
        void shouldMapDomainStudentToResponseDTOSuccessfully() {
            StudentResponseDTO result = mapper.toResponseDTO(student);

            assertNotNull(result);
            assertEquals(student.getId(), result.getId());
            assertEquals(student.getName(), result.getName());
            assertEquals(student.getCpf(), result.getCpf());
            assertEquals(student.getEmail(), result.getEmail());
            assertEquals(student.getPhone(), result.getPhone());
            assertEquals(student.getCreatedAt(), result.getCreatedAt());
            assertEquals(student.getUpdatedAt(), result.getUpdatedAt());
            assertNotNull(result.getAddress());
            assertEquals(student.getAddress().getStreet(), result.getAddress().getStreet());
        }

        @Test
        @DisplayName("Should handle null domain Student")
        void shouldHandleNullDomainStudent() {
            StudentResponseDTO result = mapper.toResponseDTO(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Should map domain Student with null address")
        void shouldMapDomainStudentWithNullAddress() {
            student.setAddress(null);

            StudentResponseDTO result = mapper.toResponseDTO(student);

            assertNotNull(result);
            assertEquals(student.getName(), result.getName());
            assertNull(result.getAddress());
        }
    }

    @Nested
    @DisplayName("Summary DTO Mapping Tests")
    class SummaryDTOMappingTests {

        @Test
        @DisplayName("Should map domain Student to StudentSummaryDTO successfully")
        void shouldMapDomainStudentToSummaryDTOSuccessfully() {
            StudentSummaryDTO result = mapper.toSummaryDTO(student);

            assertNotNull(result);
            assertEquals(student.getId(), result.getId());
            assertEquals(student.getName(), result.getName());
            assertEquals(student.getEmail(), result.getEmail());
        }

        @Test
        @DisplayName("Should handle null domain Student for summary")
        void shouldHandleNullDomainStudentForSummary() {
            StudentSummaryDTO result = mapper.toSummaryDTO(null);
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("List Mapping Tests")
    class ListMappingTests {

        @Test
        @DisplayName("Should map list of Students to list of StudentResponseDTO successfully")
        void shouldMapListOfStudentsToResponseDTOListSuccessfully() {
            Student student2 = new Student("Maria Silva", "98765432109", "maria@email.com", "(11) 88888-8888", address);
            student2.setId(2L);
            List<Student> students = Arrays.asList(student, student2);

            List<StudentResponseDTO> result = mapper.toResponseDTOList(students);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(student.getName(), result.get(0).getName());
            assertEquals(student2.getName(), result.get(1).getName());
        }

        @Test
        @DisplayName("Should map list of Students to list of StudentSummaryDTO successfully")
        void shouldMapListOfStudentsToSummaryDTOListSuccessfully() {
            Student student2 = new Student("Maria Silva", "98765432109", "maria@email.com", "(11) 88888-8888", address);
            student2.setId(2L);
            List<Student> students = Arrays.asList(student, student2);

            List<StudentSummaryDTO> result = mapper.toSummaryDTOList(students);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(student.getName(), result.get(0).getName());
            assertEquals(student2.getName(), result.get(1).getName());
        }

        @Test
        @DisplayName("Should handle null list")
        void shouldHandleNullList() {
            List<StudentResponseDTO> responseResult = mapper.toResponseDTOList(null);
            List<StudentSummaryDTO> summaryResult = mapper.toSummaryDTOList(null);

            assertNull(responseResult);
            assertNull(summaryResult);
        }

        @Test
        @DisplayName("Should handle empty list")
        void shouldHandleEmptyList() {
            List<Student> emptyList = Arrays.asList();

            List<StudentResponseDTO> responseResult = mapper.toResponseDTOList(emptyList);
            List<StudentSummaryDTO> summaryResult = mapper.toSummaryDTOList(emptyList);

            assertNotNull(responseResult);
            assertNotNull(summaryResult);
            assertTrue(responseResult.isEmpty());
            assertTrue(summaryResult.isEmpty());
        }
    }
}