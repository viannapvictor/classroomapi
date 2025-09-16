package br.edu.infnet.classroomapi.domain.entities;

import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    
    private Long id;
    private Student student;
    private Subject subject;
    private EnrollmentStatus status;
    private BigDecimal grade;
    private LocalDateTime enrollmentDate;
    private LocalDateTime completionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Enrollment(Student student, Subject subject) {
        this.student = student;
        this.subject = subject;
        this.status = EnrollmentStatus.ACTIVE;
        this.enrollmentDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void assignGrade(BigDecimal grade) {
        if (grade == null || grade.compareTo(BigDecimal.ZERO) < 0 || grade.compareTo(BigDecimal.TEN) > 0) {
            throw new IllegalArgumentException("Grade must be between 0 and 10");
        }
        this.grade = grade;
        this.updatedAt = LocalDateTime.now();

        if (grade.compareTo(new BigDecimal("7.0")) >= 0) {
            this.complete();
        }
    }

    public void complete() {
        this.status = EnrollmentStatus.COMPLETED;
        this.completionDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void suspend() {
        this.status = EnrollmentStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = EnrollmentStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reactivate() {
        this.status = EnrollmentStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isApproved() {
        return grade != null && grade.compareTo(new BigDecimal("7.0")) >= 0;
    }

    public boolean isCompleted() {
        return status == EnrollmentStatus.COMPLETED;
    }

    public boolean isActive() {
        return status == EnrollmentStatus.ACTIVE;
    }
}