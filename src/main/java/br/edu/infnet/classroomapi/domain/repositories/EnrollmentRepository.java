package br.edu.infnet.classroomapi.domain.repositories;

import br.edu.infnet.classroomapi.domain.entities.Enrollment;
import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {
    
    Enrollment save(Enrollment enrollment);
    
    Optional<Enrollment> findById(Long id);
    
    Optional<Enrollment> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
    
    List<Enrollment> findAll();
    
    List<Enrollment> findByStudentId(Long studentId);
    
    List<Enrollment> findBySubjectId(Long subjectId);
    
    List<Enrollment> findByStatus(EnrollmentStatus status);
    
    List<Enrollment> findBySubjectIdAndStatus(Long subjectId, EnrollmentStatus status);
    
    List<Enrollment> findApprovedBySubjectId(Long subjectId);
    
    List<Enrollment> findReprobedBySubjectId(Long subjectId);
    
    List<Enrollment> findByGradeGreaterThanEqual(BigDecimal grade);
    
    boolean existsByStudentIdAndSubjectId(Long studentId, Long subjectId);
    
    void deleteById(Long id);
    
    long count();
    
    long countBySubjectId(Long subjectId);
    
    long countByStatus(EnrollmentStatus status);
}