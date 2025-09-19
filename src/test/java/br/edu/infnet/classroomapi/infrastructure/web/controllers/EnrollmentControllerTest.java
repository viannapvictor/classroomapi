package br.edu.infnet.classroomapi.infrastructure.web.controllers;

import br.edu.infnet.classroomapi.application.dto.request.AssignGradeRequestDTO;
import br.edu.infnet.classroomapi.application.dto.request.CreateEnrollmentRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.*;
import br.edu.infnet.classroomapi.application.services.EnrollmentApplicationService;
import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
import br.edu.infnet.classroomapi.domain.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
@DisplayName("EnrollmentController Tests")
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnrollmentApplicationService enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateEnrollmentRequestDTO createEnrollmentRequest;
    private AssignGradeRequestDTO assignGradeRequest;
    private EnrollmentResponseDTO enrollmentResponse;
    private StudentResponseDTO studentResponse;
    private AddressResponseDTO addressResponse;
    private SubjectResponseDTO subjectResponse;
    private ProfessorResponseDTO professorResponse;

    @BeforeEach
    void setUp() {
        createEnrollmentRequest = new CreateEnrollmentRequestDTO(1L, 1L);
        assignGradeRequest = new AssignGradeRequestDTO(BigDecimal.valueOf(8.5));

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

        professorResponse = ProfessorResponseDTO.builder()
            .id(1L)
            .name("Prof. Silva")
            .email("prof@email.com")
            .role(UserRole.PROFESSOR)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        subjectResponse = SubjectResponseDTO.builder()
            .id(1L)
            .name("Java Programming")
            .code("JAVA101")
            .description("Introduction to Java")
            .workload(60)
            .professor(professorResponse)
            .enrolledStudentsCount(5L)
            .build();

        enrollmentResponse = EnrollmentResponseDTO.builder()
            .id(1L)
            .student(studentResponse)
            .subject(subjectResponse)
            .approved(true)
            .grade(BigDecimal.valueOf(8.5))
            .status(EnrollmentStatus.ACTIVE)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Nested
    @DisplayName("Create Enrollment Tests")
    class CreateEnrollmentTests {

        @Test
        @DisplayName("Should create enrollment successfully")
        void shouldCreateEnrollmentSuccessfully() throws Exception {
            when(enrollmentService.createEnrollment(any(CreateEnrollmentRequestDTO.class))).thenReturn(enrollmentResponse);

            mockMvc.perform(post("/api/v1/enrollments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createEnrollmentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.student.name").value("João Silva"))
                .andExpect(jsonPath("$.data.subject.name").value("Java Programming"))
                .andExpect(jsonPath("$.message").value("Enrollment created successfully"));
        }

        @Test
        @DisplayName("Should return validation error for invalid request")
        void shouldReturnValidationErrorForInvalidRequest() throws Exception {
            CreateEnrollmentRequestDTO invalidRequest = new CreateEnrollmentRequestDTO(null, null);

            mockMvc.perform(post("/api/v1/enrollments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Assign Grade Tests")
    class AssignGradeTests {

        @Test
        @DisplayName("Should assign grade successfully")
        void shouldAssignGradeSuccessfully() throws Exception {
            when(enrollmentService.assignGrade(anyLong(), any(AssignGradeRequestDTO.class))).thenReturn(enrollmentResponse);

            mockMvc.perform(post("/api/v1/enrollments/1/grade")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignGradeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.grade").value(8.5))
                .andExpect(jsonPath("$.message").value("Grade assigned successfully"));
        }

        @Test
        @DisplayName("Should return validation error for invalid grade")
        void shouldReturnValidationErrorForInvalidGrade() throws Exception {
            AssignGradeRequestDTO invalidRequest = new AssignGradeRequestDTO(BigDecimal.valueOf(11.0));

            mockMvc.perform(post("/api/v1/enrollments/1/grade")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Find Enrollment Tests")
    class FindEnrollmentTests {

        @Test
        @DisplayName("Should find enrollment by ID successfully")
        void shouldFindEnrollmentByIdSuccessfully() throws Exception {
            when(enrollmentService.findById(1L)).thenReturn(enrollmentResponse);

            mockMvc.perform(get("/api/v1/enrollments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.student.name").value("João Silva"));
        }

        @Test
        @DisplayName("Should find all enrollments successfully")
        void shouldFindAllEnrollmentsSuccessfully() throws Exception {
            List<EnrollmentResponseDTO> enrollments = Arrays.asList(enrollmentResponse);
            when(enrollmentService.findAll()).thenReturn(enrollments);

            mockMvc.perform(get("/api/v1/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1L));
        }

        @Test
        @DisplayName("Should find enrollments by student successfully")
        void shouldFindEnrollmentsByStudentSuccessfully() throws Exception {
            List<EnrollmentResponseDTO> enrollments = Arrays.asList(enrollmentResponse);
            when(enrollmentService.findByStudentId(1L)).thenReturn(enrollments);

            mockMvc.perform(get("/api/v1/enrollments/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].student.name").value("João Silva"));
        }

        @Test
        @DisplayName("Should find enrollments by subject successfully")
        void shouldFindEnrollmentsBySubjectSuccessfully() throws Exception {
            List<EnrollmentResponseDTO> enrollments = Arrays.asList(enrollmentResponse);
            when(enrollmentService.findBySubjectId(1L)).thenReturn(enrollments);

            mockMvc.perform(get("/api/v1/enrollments/subject/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].subject.name").value("Java Programming"));
        }

        @Test
        @DisplayName("Should find enrollments for my subjects successfully")
        void shouldFindEnrollmentsForMySubjectsSuccessfully() throws Exception {
            List<EnrollmentResponseDTO> enrollments = Arrays.asList(enrollmentResponse);
            when(enrollmentService.findByCurrentProfessorSubjects()).thenReturn(enrollments);

            mockMvc.perform(get("/api/v1/enrollments/my-subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1L));
        }

        @Test
        @DisplayName("Should find enrollments by status successfully")
        void shouldFindEnrollmentsByStatusSuccessfully() throws Exception {
            List<EnrollmentResponseDTO> enrollments = Arrays.asList(enrollmentResponse);
            when(enrollmentService.findByStatus(EnrollmentStatus.ACTIVE)).thenReturn(enrollments);

            mockMvc.perform(get("/api/v1/enrollments/status/ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].status").value("ACTIVE"));
        }

        @Test
        @DisplayName("Should find approved enrollments successfully")
        void shouldFindApprovedEnrollmentsSuccessfully() throws Exception {
            List<EnrollmentResponseDTO> enrollments = Arrays.asList(enrollmentResponse);
            when(enrollmentService.findApprovedBySubjectId(1L)).thenReturn(enrollments);

            mockMvc.perform(get("/api/v1/enrollments/subject/1/approved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].approved").value(true));
        }

        @Test
        @DisplayName("Should find reproved enrollments successfully")
        void shouldFindReprovedEnrollmentsSuccessfully() throws Exception {
            EnrollmentResponseDTO reprovedResponse = EnrollmentResponseDTO.builder()
                .id(2L)
                .student(studentResponse)
                .subject(subjectResponse)
                .approved(false)
                .grade(BigDecimal.valueOf(4.0))
                .status(EnrollmentStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            List<EnrollmentResponseDTO> enrollments = Arrays.asList(reprovedResponse);
            when(enrollmentService.findReprobedBySubjectId(1L)).thenReturn(enrollments);

            mockMvc.perform(get("/api/v1/enrollments/subject/1/reproved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].approved").value(false));
        }
    }

    @Nested
    @DisplayName("Update Enrollment Tests")
    class UpdateEnrollmentTests {

        @Test
        @DisplayName("Should suspend enrollment successfully")
        void shouldSuspendEnrollmentSuccessfully() throws Exception {
            EnrollmentResponseDTO suspendedResponse = EnrollmentResponseDTO.builder()
                .id(1L)
                .student(studentResponse)
                .subject(subjectResponse)
                .approved(true)
                .grade(BigDecimal.valueOf(8.5))
                .status(EnrollmentStatus.SUSPENDED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            when(enrollmentService.suspendEnrollment(1L)).thenReturn(suspendedResponse);

            mockMvc.perform(put("/api/v1/enrollments/1/suspend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("SUSPENDED"))
                .andExpect(jsonPath("$.message").value("Enrollment suspended successfully"));
        }

        @Test
        @DisplayName("Should reactivate enrollment successfully")
        void shouldReactivateEnrollmentSuccessfully() throws Exception {
            when(enrollmentService.reactivateEnrollment(1L)).thenReturn(enrollmentResponse);

            mockMvc.perform(put("/api/v1/enrollments/1/reactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andExpect(jsonPath("$.message").value("Enrollment reactivated successfully"));
        }
    }

    @Nested
    @DisplayName("Delete Enrollment Tests")
    class DeleteEnrollmentTests {

        @Test
        @DisplayName("Should delete enrollment successfully")
        void shouldDeleteEnrollmentSuccessfully() throws Exception {
            doNothing().when(enrollmentService).deleteEnrollment(1L);

            mockMvc.perform(delete("/api/v1/enrollments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("Enrollment deleted successfully"));
        }
    }
}