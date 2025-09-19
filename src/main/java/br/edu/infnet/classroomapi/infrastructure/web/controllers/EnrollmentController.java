package br.edu.infnet.classroomapi.infrastructure.web.controllers;

import br.edu.infnet.classroomapi.application.dto.request.AssignGradeRequestDTO;
import br.edu.infnet.classroomapi.application.dto.request.CreateEnrollmentRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.EnrollmentResponseDTO;
import br.edu.infnet.classroomapi.application.services.EnrollmentApplicationService;
import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
import br.edu.infnet.classroomapi.infrastructure.web.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentApplicationService enrollmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> createEnrollment(@Valid @RequestBody CreateEnrollmentRequestDTO request) {
        EnrollmentResponseDTO enrollmentResponse = enrollmentService.createEnrollment(request);
        ApiResponse<EnrollmentResponseDTO> response = ApiResponse.success(enrollmentResponse, "Enrollment created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/grade")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> assignGrade(
            @PathVariable Long id,
            @Valid @RequestBody AssignGradeRequestDTO request) {
        EnrollmentResponseDTO enrollmentResponse = enrollmentService.assignGrade(id, request);
        ApiResponse<EnrollmentResponseDTO> response = ApiResponse.success(enrollmentResponse, "Grade assigned successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> getEnrollmentById(@PathVariable Long id) {
        EnrollmentResponseDTO enrollmentResponse = enrollmentService.findById(id);
        ApiResponse<EnrollmentResponseDTO> response = ApiResponse.success(enrollmentResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getAllEnrollments() {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findAll();
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findByStudentId(studentId);
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsBySubject(@PathVariable Long subjectId) {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findBySubjectId(subjectId);
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-subjects")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsForMySubjects() {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findByCurrentProfessorSubjects();
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getEnrollmentsByStatus(@PathVariable EnrollmentStatus status) {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findByStatus(status);
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subject/{subjectId}/approved")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getApprovedEnrollments(@PathVariable Long subjectId) {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findApprovedBySubjectId(subjectId);
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subject/{subjectId}/reproved")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> getReprovedEnrollments(@PathVariable Long subjectId) {
        List<EnrollmentResponseDTO> enrollmentsResponse = enrollmentService.findReprobedBySubjectId(subjectId);
        ApiResponse<List<EnrollmentResponseDTO>> response = ApiResponse.success(enrollmentsResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> suspendEnrollment(@PathVariable Long id) {
        EnrollmentResponseDTO enrollmentResponse = enrollmentService.suspendEnrollment(id);
        ApiResponse<EnrollmentResponseDTO> response = ApiResponse.success(enrollmentResponse, "Enrollment suspended successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/reactivate")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> reactivateEnrollment(@PathVariable Long id) {
        EnrollmentResponseDTO enrollmentResponse = enrollmentService.reactivateEnrollment(id);
        ApiResponse<EnrollmentResponseDTO> response = ApiResponse.success(enrollmentResponse, "Enrollment reactivated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Enrollment deleted successfully");
        return ResponseEntity.ok(response);
    }
}