package br.edu.infnet.classroomapi.infrastructure.web.controllers;

import br.edu.infnet.classroomapi.application.dto.request.CreateStudentRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentSummaryDTO;
import br.edu.infnet.classroomapi.application.services.StudentApplicationService;
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
@RequestMapping("/v1/students")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Students", description = "Student management operations")
public class StudentController {

    private final StudentApplicationService studentService;

    @PostMapping
    @Operation(summary = "Create student", description = "Create a new student")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> createStudent(@Valid @RequestBody CreateStudentRequestDTO request) {
        StudentResponseDTO studentResponse = studentService.createStudent(request);
        ApiResponse<StudentResponseDTO> response = ApiResponse.success(studentResponse, "Student created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Find student by ID")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(@PathVariable Long id) {
        StudentResponseDTO studentResponse = studentService.findById(id);
        ApiResponse<StudentResponseDTO> response = ApiResponse.success(studentResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Get student by CPF", description = "Find student by CPF")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentByCpf(@PathVariable String cpf) {
        StudentResponseDTO studentResponse = studentService.findByCpf(cpf);
        ApiResponse<StudentResponseDTO> response = ApiResponse.success(studentResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get student by email", description = "Find student by email")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentByEmail(@PathVariable String email) {
        StudentResponseDTO studentResponse = studentService.findByEmail(email);
        ApiResponse<StudentResponseDTO> response = ApiResponse.success(studentResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all students", description = "Get list of all students")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents() {
        List<StudentResponseDTO> studentsResponse = studentService.findAll();
        ApiResponse<List<StudentResponseDTO>> response = ApiResponse.success(studentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get students summary", description = "Get summary list of all students")
    public ResponseEntity<ApiResponse<List<StudentSummaryDTO>>> getStudentsSummary() {
        List<StudentSummaryDTO> summaryResponse = studentService.findAllSummary();
        ApiResponse<List<StudentSummaryDTO>> response = ApiResponse.success(summaryResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search students by name", description = "Search students by name (case insensitive)")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> searchStudentsByName(@RequestParam String name) {
        List<StudentResponseDTO> studentsResponse = studentService.findByName(name);
        ApiResponse<List<StudentResponseDTO>> response = ApiResponse.success(studentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subject/{subjectId}")
    @Operation(summary = "Get students by subject", description = "Get students enrolled in specific subject")
    public ResponseEntity<ApiResponse<List<StudentSummaryDTO>>> getStudentsBySubject(@PathVariable Long subjectId) {
        List<StudentSummaryDTO> studentsResponse = studentService.findBySubjectId(subjectId);
        ApiResponse<List<StudentSummaryDTO>> response = ApiResponse.success(studentsResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student", description = "Update student information")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody CreateStudentRequestDTO request) {
        StudentResponseDTO studentResponse = studentService.updateStudent(id, request);
        ApiResponse<StudentResponseDTO> response = ApiResponse.success(studentResponse, "Student updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student", description = "Delete student by ID")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteById(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Student deleted successfully");
        return ResponseEntity.ok(response);
    }
}