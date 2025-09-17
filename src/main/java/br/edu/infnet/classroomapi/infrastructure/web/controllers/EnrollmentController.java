package br.edu.infnet.classroomapi.infrastructure.web.controllers;

import br.edu.infnet.classroomapi.application.dto.request.AssignGradeRequestDTO;
import br.edu.infnet.classroomapi.application.dto.request.CreateEnrollmentRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.EnrollmentResponseDTO;
import br.edu.infnet.classroomapi.application.services.EnrollmentApplicationService;
import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
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
@RequestMapping("/v1/enrollments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Enrollments", description = "Enrollment management operations")
public class EnrollmentController {

    private final EnrollmentApplicationService enrollmentService;

    @PostMapping
    @Operation(summary = "Create enrollment", description = "Enroll student in subject")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> createEnrollment(@Valid @RequestBody CreateEnrollmentRequestDTO request) {
        EnrollmentResponseDTO enrollmentResponse = enrollmentService.createEnrollment(request);
        ApiResponse<EnrollmentResponseDTO> response = ApiResponse.success(enrollmentResponse, "Enrollment created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/grade")
    @Operation(summary = "Assign grade", description = "Assign grade to enrollment")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> assignGrade(
            @PathVariable Long id,
            @Valid @RequestBody AssignGradeRequestDTO request) {
        EnrollmentResponseDTO enrollmentResponse = enrollmentService.assignGrade(id, request);
        ApiResponse<EnrollmentResponseDTO> response = ApiResponse.success(enrollmentResponse, "Grade assigned successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID", description = "Find enrollment by ID")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> getEnrollmentById(@PathVariable Long id) {
        EnrollmentResponseDTO enrollmentResponse = enrollmentService.findById(id);
        ApiResponse<EnrollmentResponseDTO> response = ApiResponse.success(enrollmentResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all enrollments", description = "Get list of all enrollments")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getAllEnrollments() {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findAll();
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get enrollments by student", description = "Get enrollments for specific student")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findByStudentId(studentId);
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subject/{subjectId}")
    @Operation(summary = "Get enrollments by subject", description = "Get enrollments for specific subject")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsBySubject(@PathVariable Long subjectId) {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findBySubjectId(subjectId);
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-subjects")
    @Operation(summary = "Get enrollments for my subjects", description = "Get enrollments for current professor's subjects")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsForMySubjects() {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findByCurrentProfessorSubjects();
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get enrollments by status", description = "Get enrollments by status")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByStatus(@PathVariable EnrollmentStatus status) {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findByStatus(status);
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subject/{subjectId}/approved")
    @Operation(summary = "Get approved enrollments", description = "Get approved enrollments for subject")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getApprovedEnrollments(@PathVariable Long subjectId) {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findApprovedBySubjectId(subjectId);
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subject/{subjectId}/reproved")
    @Operation(summary = "Get reproved enrollments", description = "Get reproved enrollments for subject")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getReprovedEnrollments(@PathVariable Long subjectId) {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findReprobedBySubjectId(subjectId);
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/suspend")
    @Operation(summary = "Suspend enrollment", description = "Suspend enrollment")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> suspendEnrollment(@PathVariable Long id) {
        EnrollmentResponseDTO enrollmentResponse = enrollmentService.suspendEnrollment(id);
        ApiResponse<EnrollmentResponseDTO> response = ApiResponse.success(enrollmentResponse, "Enrollment suspended successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/reactivate")
    @Operation(summary = "Reactivate enrollment", description = "Reactivate enrollment")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> reactivateEnrollment(@PathVariable Long id) {
        EnrollmentResponseDTO enrollmentResponse = enrollmentService.reactivateEnrollment(id);
        ApiResponse<EnrollmentResponseDTO> response = ApiResponse.success(enrollmentResponse, "Enrollment reactivated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete enrollment", description = "Delete enrollment")
    public ResponseEntity<ApiResponse<Void>> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Enrollment deleted successfully");
        return ResponseEntity.ok(response);
    }
}