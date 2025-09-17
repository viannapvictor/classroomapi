package br.edu.infnet.classroomapi.domain.services;

import br.edu.infnet.classroomapi.domain.entities.Enrollment;
import br.edu.infnet.classroomapi.domain.entities.Student;
import br.edu.infnet.classroomapi.domain.entities.Subject;
import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class EnrollmentDomainService {
    
    public static Enrollment createEnrollment(Student student, Subject subject) {
        validateEnrollmentCreation(student, subject);
        
        Enrollment enrollment = new Enrollment(student, subject);

        student.addEnrollment(enrollment);
        subject.addEnrollment(enrollment);
        
        return enrollment;
    }
    
    public static void assignGrade(Enrollment enrollment, BigDecimal grade) {
        validateGradeAssignment(enrollment, grade);
        enrollment.assignGrade(grade);
    }
    
    public static boolean canEnroll(Student student, Subject subject) {
        if (student == null || subject == null) {
            return false;
        }

        return student.getEnrollments().stream()
                .noneMatch(e -> e.getSubject().getId().equals(subject.getId())
                        && e.getStatus() == EnrollmentStatus.ACTIVE);
    }
    
    private static void validateEnrollmentCreation(Student student, Subject subject) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        }
        if (!canEnroll(student, subject)) {
            throw new IllegalArgumentException("Student is already enrolled in this subject");
        }
    }
    
    private static void validateGradeAssignment(Enrollment enrollment, BigDecimal grade) {
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment cannot be null");
        }
        if (!enrollment.isActive()) {
            throw new IllegalArgumentException("Can only assign grades to active enrollments");
        }
        if (grade == null) {
            throw new IllegalArgumentException("Grade cannot be null");
        }
        if (grade.compareTo(BigDecimal.ZERO) < 0 || grade.compareTo(BigDecimal.TEN) > 0) {
            throw new IllegalArgumentException("Grade must be between 0 and 10");
        }
    }
}