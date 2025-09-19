package br.edu.infnet.classroomapi.infrastructure.web.controllers;

import br.edu.infnet.classroomapi.application.dto.request.CreateStudentRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentSummaryDTO;
import br.edu.infnet.classroomapi.application.services.StudentApplicationService;
import br.edu.infnet.classroomapi.infrastructure.web.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentApplicationService studentService;

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> createStudent(@Valid @RequestBody CreateStudentRequestDTO request) {
        StudentResponseDTO studentResponse = studentService.createStudent(request);
        ApiResponse<StudentResponseDTO> response = ApiResponse.success(studentResponse, "Student created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(@PathVariable Long id) {
        StudentResponseDTO studentResponse = studentService.findById(id);
        ApiResponse<StudentResponseDTO> response = ApiResponse.success(studentResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentByCpf(@PathVariable String cpf) {
        StudentResponseDTO studentResponse = studentService.findByCpf(cpf);
        ApiResponse<StudentResponseDTO> response = ApiResponse.success(studentResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentByEmail(@PathVariable String email) {
        StudentResponseDTO studentResponse = studentService.findByEmail(email);
        ApiResponse<StudentResponseDTO> response = ApiResponse.success(studentResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents() {
        List<StudentResponseDTO> studentsResponse = studentService.findAll();
        ApiResponse<List<StudentResponseDTO>> response = ApiResponse.success(studentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<List<StudentSummaryDTO>>> getStudentsSummary() {
        List<StudentSummaryDTO> summaryResponse = studentService.findAllSummary();
        ApiResponse<List<StudentSummaryDTO>> response = ApiResponse.success(summaryResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> searchStudentsByName(@RequestParam String name) {
        List<StudentResponseDTO> studentsResponse = studentService.findByName(name);
        ApiResponse<List<StudentResponseDTO>> response = ApiResponse.success(studentsResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<ApiResponse<List<StudentSummaryDTO>>> getStudentsBySubject(@PathVariable Long subjectId) {
        List<StudentSummaryDTO> studentsResponse = studentService.findBySubjectId(subjectId);
        ApiResponse<List<StudentSummaryDTO>> response = ApiResponse.success(studentsResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody CreateStudentRequestDTO request) {
        StudentResponseDTO studentResponse = studentService.updateStudent(id, request);
        ApiResponse<StudentResponseDTO> response = ApiResponse.success(studentResponse, "Student updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteById(id);
        ApiResponse<Void> response = ApiResponse.success(null, "Student deleted successfully");
        return ResponseEntity.ok(response);
    }
}