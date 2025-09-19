package br.edu.infnet.classroomapi.infrastructure.web.controllers;

import br.edu.infnet.classroomapi.application.dto.request.CreateSubjectRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectSummaryDTO;
import br.edu.infnet.classroomapi.application.services.SubjectApplicationService;
import br.edu.infnet.classroomapi.infrastructure.web.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectApplicationService subjectService;

    @PostMapping
    public ResponseEntity<ApiResponse<SubjectResponseDTO>> createSubject(@Valid @RequestBody CreateSubjectRequestDTO request) {
        SubjectResponseDTO subjectResponse = subjectService.createSubject(request);
        ApiResponse<SubjectResponseDTO> response = ApiResponse.success(subjectResponse, "Subject created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponseDTO>> getSubjectById(@PathVariable Long id) {
        SubjectResponseDTO subjectResponse = subjectService.findById(id);
        ApiResponse<SubjectResponseDTO> response = ApiResponse.success(subjectResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<SubjectResponseDTO>> getSubjectByCode(@PathVariable String code) {
        SubjectResponseDTO subjectResponse = subjectService.findByCode(code);
        ApiResponse<SubjectResponseDTO> response = ApiResponse.success(subjectResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectResponseDTO>>> getAllSubjects() {
        List<SubjectResponseDTO> subjectsResponse = subjectService.findAll();
        ApiResponse<List<SubjectResponseDTO>> response = ApiResponse.success(subjectsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<List<SubjectSummaryDTO>>> getSubjectsSummary() {
        List<SubjectSummaryDTO> summaryResponse = subjectService.findAllSummary();
        ApiResponse<List<SubjectSummaryDTO>> response = ApiResponse.success(summaryResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-subjects")
    public ResponseEntity<ApiResponse<List<SubjectResponseDTO>>> getMySubjects() {
        List<SubjectResponseDTO> subjectsResponse = subjectService.findByCurrentProfessor();
        ApiResponse<List<SubjectResponseDTO>> response = ApiResponse.success(subjectsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<ApiResponse<List<SubjectResponseDTO>>> getSubjectsByProfessor(@PathVariable Long professorId) {
        List<SubjectResponseDTO> subjectsResponse = subjectService.findByProfessorId(professorId);
        ApiResponse<List<SubjectResponseDTO>> response = ApiResponse.success(subjectsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SubjectResponseDTO>>> searchSubjectsByName(@RequestParam String name) {
        List<SubjectResponseDTO> subjectsResponse = subjectService.findByName(name);
        ApiResponse<List<SubjectResponseDTO>> response = ApiResponse.success(subjectsResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponseDTO>> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody CreateSubjectRequestDTO request) {
        SubjectResponseDTO subjectResponse = subjectService.updateSubject(id, request);
        ApiResponse<SubjectResponseDTO> response = ApiResponse.success(subjectResponse, "Subject updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(@PathVariable Long id) {
        subjectService.deleteById(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Subject deleted successfully");
        return ResponseEntity.ok(response);
    }
}