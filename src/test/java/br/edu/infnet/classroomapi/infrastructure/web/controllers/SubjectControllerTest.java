package br.edu.infnet.classroomapi.infrastructure.web.controllers;

import br.edu.infnet.classroomapi.application.dto.request.CreateSubjectRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.ProfessorResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectSummaryDTO;
import br.edu.infnet.classroomapi.application.services.SubjectApplicationService;
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
@DisplayName("SubjectController Tests")
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubjectApplicationService subjectService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateSubjectRequestDTO createSubjectRequest;
    private SubjectResponseDTO subjectResponse;
    private SubjectSummaryDTO subjectSummary;
    private ProfessorResponseDTO professorResponse;

    @BeforeEach
    void setUp() {
        createSubjectRequest = new CreateSubjectRequestDTO(
            "Java Programming", "JAVA101", "Introduction to Java", 60
        );

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

        subjectSummary = SubjectSummaryDTO.builder()
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
        void shouldCreateSubjectSuccessfully() throws Exception {
            when(subjectService.createSubject(any(CreateSubjectRequestDTO.class))).thenReturn(subjectResponse);

            mockMvc.perform(post("/api/v1/subjects")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createSubjectRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Java Programming"))
                .andExpect(jsonPath("$.data.code").value("JAVA101"))
                .andExpect(jsonPath("$.data.workload").value(60))
                .andExpect(jsonPath("$.message").value("Subject created successfully"));
        }

        @Test
        @DisplayName("Should return validation error for invalid request")
        void shouldReturnValidationErrorForInvalidRequest() throws Exception {
            CreateSubjectRequestDTO invalidRequest = new CreateSubjectRequestDTO(
                "", "", "", -1
            );

            mockMvc.perform(post("/api/v1/subjects")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Find Subject Tests")
    class FindSubjectTests {

        @Test
        @DisplayName("Should find subject by ID successfully")
        void shouldFindSubjectByIdSuccessfully() throws Exception {
            when(subjectService.findById(1L)).thenReturn(subjectResponse);

            mockMvc.perform(get("/api/v1/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Java Programming"));
        }

        @Test
        @DisplayName("Should find subject by code successfully")
        void shouldFindSubjectByCodeSuccessfully() throws Exception {
            when(subjectService.findByCode("JAVA101")).thenReturn(subjectResponse);

            mockMvc.perform(get("/api/v1/subjects/code/JAVA101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value("JAVA101"));
        }

        @Test
        @DisplayName("Should find all subjects successfully")
        void shouldFindAllSubjectsSuccessfully() throws Exception {
            List<SubjectResponseDTO> subjects = Arrays.asList(subjectResponse);
            when(subjectService.findAll()).thenReturn(subjects);

            mockMvc.perform(get("/api/v1/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("Java Programming"));
        }

        @Test
        @DisplayName("Should get subjects summary successfully")
        void shouldGetSubjectsSummarySuccessfully() throws Exception {
            List<SubjectSummaryDTO> summaries = Arrays.asList(subjectSummary);
            when(subjectService.findAllSummary()).thenReturn(summaries);

            mockMvc.perform(get("/api/v1/subjects/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("Java Programming"));
        }

        @Test
        @DisplayName("Should get current professor subjects successfully")
        void shouldGetCurrentProfessorSubjectsSuccessfully() throws Exception {
            List<SubjectResponseDTO> subjects = Arrays.asList(subjectResponse);
            when(subjectService.findByCurrentProfessor()).thenReturn(subjects);

            mockMvc.perform(get("/api/v1/subjects/my-subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("Java Programming"));
        }

        @Test
        @DisplayName("Should get subjects by professor successfully")
        void shouldGetSubjectsByProfessorSuccessfully() throws Exception {
            List<SubjectResponseDTO> subjects = Arrays.asList(subjectResponse);
            when(subjectService.findByProfessorId(1L)).thenReturn(subjects);

            mockMvc.perform(get("/api/v1/subjects/professor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("Java Programming"));
        }

        @Test
        @DisplayName("Should search subjects by name successfully")
        void shouldSearchSubjectsByNameSuccessfully() throws Exception {
            List<SubjectResponseDTO> subjects = Arrays.asList(subjectResponse);
            when(subjectService.findByName("Java")).thenReturn(subjects);

            mockMvc.perform(get("/api/v1/subjects/search").param("name", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("Java Programming"));
        }
    }

    @Nested
    @DisplayName("Update Subject Tests")
    class UpdateSubjectTests {

        @Test
        @DisplayName("Should update subject successfully")
        void shouldUpdateSubjectSuccessfully() throws Exception {
            SubjectResponseDTO updatedResponse = SubjectResponseDTO.builder()
                .id(1L)
                .name("Advanced Java Programming")
                .code("JAVA101")
                .description("Advanced Java concepts")
                .workload(80)
                .professor(professorResponse)
                .enrolledStudentsCount(5L)
                .build();

            CreateSubjectRequestDTO updateRequest = new CreateSubjectRequestDTO(
                "Advanced Java Programming", "JAVA101", "Advanced Java concepts", 80
            );

            when(subjectService.updateSubject(anyLong(), any(CreateSubjectRequestDTO.class))).thenReturn(updatedResponse);

            mockMvc.perform(put("/api/v1/subjects/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Advanced Java Programming"))
                .andExpect(jsonPath("$.data.description").value("Advanced Java concepts"))
                .andExpect(jsonPath("$.data.workload").value(80))
                .andExpect(jsonPath("$.message").value("Subject updated successfully"));
        }

        @Test
        @DisplayName("Should return validation error for invalid update request")
        void shouldReturnValidationErrorForInvalidUpdateRequest() throws Exception {
            CreateSubjectRequestDTO invalidRequest = new CreateSubjectRequestDTO(
                "", "", "", -1
            );

            mockMvc.perform(put("/api/v1/subjects/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Delete Subject Tests")
    class DeleteSubjectTests {

        @Test
        @DisplayName("Should delete subject successfully")
        void shouldDeleteSubjectSuccessfully() throws Exception {
            doNothing().when(subjectService).deleteById(1L);

            mockMvc.perform(delete("/api/v1/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value("Subject deleted successfully"));
        }
    }
}