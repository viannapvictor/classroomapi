package br.edu.infnet.classroomapi.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    
    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer workload;
    private Professor professor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Enrollment> enrollments;

    public Subject(String name, String code, String description, Integer workload) {
        validateRequiredFields(name, code);
        this.name = name;
        this.code = code;
        this.description = description;
        this.workload = workload;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.enrollments = new ArrayList<>();
    }

    private void validateRequiredFields(String name, String code) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be null or empty");
        }
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject code cannot be null or empty");
        }
    }

    public void addEnrollment(Enrollment enrollment) {
        if (this.enrollments == null) {
            this.enrollments = new ArrayList<>();
        }
        this.enrollments.add(enrollment);
    }

    public void updateInfo(String name, String description, Integer workload) {
        this.name = name;
        this.description = description;
        this.workload = workload;
        this.updatedAt = LocalDateTime.now();
    }

    public long getEnrolledStudentsCount() {
        return enrollments != null ? enrollments.size() : 0;
    }
}