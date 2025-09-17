package br.edu.infnet.classroomapi.infrastructure.web.controllers;

import br.edu.infnet.classroomapi.application.dto.request.CreateSubjectRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectSummaryDTO;
import br.edu.infnet.classroomapi.application.services.SubjectApplicationService;
import br.edu.infnet.classroomapi.infrastructure.web.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/subjects")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Subjects", description = "Subject management operations")
public class SubjectController {

    private final SubjectApplicationService subjectService;

    @PostMapping
    @Operation(summary = "Create subject", description = "Create a new subject for current professor")
    public ResponseEntity<ApiResponse<SubjectResponseDTO>> createSubject(@Valid @RequestBody CreateSubjectRequestDTO request) {
        SubjectResponseDTO subjectResponse = subjectService.createSubject(request);
        ApiResponse<SubjectResponseDTO> response = ApiResponse.success(subjectResponse, "Subject created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by ID", description = "Find subject by ID")
    public ResponseEntity<ApiResponse<SubjectResponseDTO>> getSubjectById(@PathVariable Long id) {
        SubjectResponseDTO subjectResponse = subjectService.findById(id);
        ApiResponse<SubjectResponseDTO> response = ApiResponse.success(subjectResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get subject by code", description = "Find subject by code")
    public ResponseEntity<ApiResponse<SubjectResponseDTO>> getSubjectByCode(@PathVariable String code) {
        SubjectResponseDTO subjectResponse = subjectService.findByCode(code);
        ApiResponse<SubjectResponseDTO> response = ApiResponse.success(subjectResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all subjects", description = "Get list of all subjects")
    public ResponseEntity<ApiResponse<List<SubjectResponseDTO>>> getAllSubjects() {
        List<SubjectResponseDTO> subjectsResponse = subjectService.findAll();
        ApiResponse<List<SubjectResponseDTO>> response = ApiResponse.success(subjectsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get subjects summary", description = "Get summary list of all subjects")
    public ResponseEntity<ApiResponse<List<SubjectSummaryDTO>>> getSubjectsSummary() {
        List<SubjectSummaryDTO> summaryResponse = subjectService.findAllSummary();
        ApiResponse<List<SubjectSummaryDTO>> response = ApiResponse.success(summaryResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-subjects")
    @Operation(summary = "Get current professor subjects", description = "Get subjects created by current professor")
    public ResponseEntity<ApiResponse<List<SubjectResponseDTO>>> getMySubjects() {
        List<SubjectResponseDTO> subjectsResponse = subjectService.findByCurrentProfessor();
        ApiResponse<List<SubjectResponseDTO>> response = ApiResponse.success(subjectsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/professor/{professorId}")
    @Operation(summary = "Get subjects by professor", description = "Get subjects by professor ID")
    public ResponseEntity<ApiResponse<List<SubjectResponseDTO>>> getSubjectsByProfessor(@PathVariable Long professorId) {
        List<SubjectResponseDTO> subjectsResponse = subjectService.findByProfessorId(professorId);
        ApiResponse<List<SubjectResponseDTO>> response = ApiResponse.success(subjectsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search subjects by name", description = "Search subjects by name (case insensitive)")
    public ResponseEntity<ApiResponse<List<SubjectResponseDTO>>> searchSubjectsByName(@RequestParam String name) {
        List<SubjectResponseDTO> subjectsResponse = subjectService.findByName(name);
        ApiResponse<List<SubjectResponseDTO>> response = ApiResponse.success(subjectsResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update subject", description = "Update subject information (only own subjects)")
    public ResponseEntity<ApiResponse<SubjectResponseDTO>> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody CreateSubjectRequestDTO request) {
        SubjectResponseDTO subjectResponse = subjectService.updateSubject(id, request);
        ApiResponse<SubjectResponseDTO> response = ApiResponse.success(subjectResponse, "Subject updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subject", description = "Delete subject by ID (only own subjects)")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(@PathVariable Long id) {
        subjectService.deleteById(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Subject deleted successfully");
        return ResponseEntity.ok(response);
    }
}