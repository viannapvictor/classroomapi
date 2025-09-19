package br.edu.infnet.classroomapi.infrastructure.web.controllers;

import br.edu.infnet.classroomapi.application.dto.request.AddressRequestDTO;
import br.edu.infnet.classroomapi.application.dto.request.CreateStudentRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.AddressResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentSummaryDTO;
import br.edu.infnet.classroomapi.application.services.StudentApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@DisplayName("StudentController Tests")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentApplicationService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateStudentRequestDTO createStudentRequest;
    private StudentResponseDTO studentResponse;
    private StudentSummaryDTO studentSummary;
    private AddressRequestDTO addressRequest;
    private AddressResponseDTO addressResponse;

    @BeforeEach
    void setUp() {
        addressRequest = new AddressRequestDTO(
            "Rua das Flores", "123", "Apto 45", "Centro",
            "São Paulo", "SP", "01234-567", "Brasil"
        );

        createStudentRequest = new CreateStudentRequestDTO(
            "João Silva", "12345678901", "joao@email.com", "(11) 99999-9999", addressRequest
        );

        addressResponse = AddressResponseDTO.builder()
            .street("Rua das Flores")
            .number("123")
            .complement("Apto 45")
            .neighborhood("Centro")
            .city("São Paulo")
            .state("SP")
            .zipCode("01234-567")
            .country("Brasil")
            .fullAddress("Rua das Flores, 123, Apto 45, Centro, São Paulo, SP, 01234-567, Brasil")
            .build();

        studentResponse = StudentResponseDTO.builder()
            .id(1L)
            .name("João Silva")
            .cpf("12345678901")
            .email("joao@email.com")
            .phone("(11) 99999-9999")
            .address(addressResponse)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        studentSummary = StudentSummaryDTO.builder()
            .id(1L)
            .name("João Silva")
            .email("joao@email.com")
            .build();
    }

    @Nested
    @DisplayName("Create Student Tests")
    class CreateStudentTests {

        @Test
        @DisplayName("Should create student successfully")
        void shouldCreateStudentSuccessfully() throws Exception {
            when(studentService.createStudent(any(CreateStudentRequestDTO.class))).thenReturn(studentResponse);

            mockMvc.perform(post("/api/v1/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createStudentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("João Silva"))
                .andExpect(jsonPath("$.data.cpf").value("12345678901"))
                .andExpect(jsonPath("$.data.email").value("joao@email.com"))
                .andExpect(jsonPath("$.message").value("Student created successfully"));
        }

        @Test
        @DisplayName("Should return validation error for invalid request")
        void shouldReturnValidationErrorForInvalidRequest() throws Exception {
            CreateStudentRequestDTO invalidRequest = new CreateStudentRequestDTO(
                "", "", "invalid-email", null, null
            );

            mockMvc.perform(post("/api/v1/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Find Student Tests")
    class FindStudentTests {

        @Test
        @DisplayName("Should find student by ID successfully")
        void shouldFindStudentByIdSuccessfully() throws Exception {
            when(studentService.findById(1L)).thenReturn(studentResponse);

            mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("João Silva"));
        }

        @Test
        @DisplayName("Should find student by CPF successfully")
        void shouldFindStudentByCpfSuccessfully() throws Exception {
            when(studentService.findByCpf("12345678901")).thenReturn(studentResponse);

            mockMvc.perform(get("/api/v1/students/cpf/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cpf").value("12345678901"));
        }

        @Test
        @DisplayName("Should find student by email successfully")
        void shouldFindStudentByEmailSuccessfully() throws Exception {
            when(studentService.findByEmail("joao@email.com")).thenReturn(studentResponse);

            mockMvc.perform(get("/api/v1/students/email/joao@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("joao@email.com"));
        }

        @Test
        @DisplayName("Should find all students successfully")
        void shouldFindAllStudentsSuccessfully() throws Exception {
            List<StudentResponseDTO> students = Arrays.asList(studentResponse);
            when(studentService.findAll()).thenReturn(students);

            mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("João Silva"));
        }

        @Test
        @DisplayName("Should get students summary successfully")
        void shouldGetStudentsSummarySuccessfully() throws Exception {
            List<StudentSummaryDTO> summaries = Arrays.asList(studentSummary);
            when(studentService.findAllSummary()).thenReturn(summaries);

            mockMvc.perform(get("/api/v1/students/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("João Silva"));
        }

        @Test
        @DisplayName("Should search students by name successfully")
        void shouldSearchStudentsByNameSuccessfully() throws Exception {
            List<StudentResponseDTO> students = Arrays.asList(studentResponse);
            when(studentService.findByName("João")).thenReturn(students);

            mockMvc.perform(get("/api/v1/students/search").param("name", "João"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("João Silva"));
        }

        @Test
        @DisplayName("Should get students by subject successfully")
        void shouldGetStudentsBySubjectSuccessfully() throws Exception {
            List<StudentSummaryDTO> students = Arrays.asList(studentSummary);
            when(studentService.findBySubjectId(1L)).thenReturn(students);

            mockMvc.perform(get("/api/v1/students/subject/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("João Silva"));
        }
    }

    @Nested
    @DisplayName("Update Student Tests")
    class UpdateStudentTests {

        @Test
        @DisplayName("Should update student successfully")
        void shouldUpdateStudentSuccessfully() throws Exception {
            StudentResponseDTO updatedResponse = StudentResponseDTO.builder()
                .id(1L)
                .name("João Silva Updated")
                .cpf("12345678901")
                .email("joao.updated@email.com")
                .phone("(11) 99999-9999")
                .address(addressResponse)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            CreateStudentRequestDTO updateRequest = new CreateStudentRequestDTO(
                "João Silva Updated", "12345678901", "joao.updated@email.com", "(11) 99999-9999", addressRequest
            );

            when(studentService.updateStudent(anyLong(), any(CreateStudentRequestDTO.class))).thenReturn(updatedResponse);

            mockMvc.perform(put("/api/v1/students/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("João Silva Updated"))
                .andExpect(jsonPath("$.data.email").value("joao.updated@email.com"))
                .andExpect(jsonPath("$.message").value("Student updated successfully"));
        }

        @Test
        @DisplayName("Should return validation error for invalid update request")
        void shouldReturnValidationErrorForInvalidUpdateRequest() throws Exception {
            CreateStudentRequestDTO invalidRequest = new CreateStudentRequestDTO(
                "", "", "invalid-email", null, null
            );

            mockMvc.perform(put("/api/v1/students/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Delete Student Tests")
    class DeleteStudentTests {

        @Test
        @DisplayName("Should delete student successfully")
        void shouldDeleteStudentSuccessfully() throws Exception {
            doNothing().when(studentService).deleteById(1L);

            mockMvc.perform(delete("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("Student deleted successfully"));
        }
    }
}