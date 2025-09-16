package br.edu.infnet.classroomapi.infrastructure.persistence.repositories;

import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
import br.edu.infnet.classroomapi.infrastructure.persistence.entities.EnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentEntity, Long> {
    
    Optional<EnrollmentEntity> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
    
    List<EnrollmentEntity> findByStudentId(Long studentId);
    
    List<EnrollmentEntity> findBySubjectId(Long subjectId);
    
    List<EnrollmentEntity> findByStatus(EnrollmentStatus status);
    
    List<EnrollmentEntity> findBySubjectIdAndStatus(Long subjectId, EnrollmentStatus status);
    
    @Query("SELECT e FROM EnrollmentEntity e " +
           "WHERE e.subject.id = :subjectId " +
           "AND e.grade >= 7.0")
    List<EnrollmentEntity> findApprovedBySubjectId(@Param("subjectId") Long subjectId);
    
    @Query("SELECT e FROM EnrollmentEntity e " +
           "WHERE e.subject.id = :subjectId " +
           "AND e.grade < 7.0 " +
           "AND e.grade IS NOT NULL")
    List<EnrollmentEntity> findReprobedBySubjectId(@Param("subjectId") Long subjectId);
    
    List<EnrollmentEntity> findByGradeGreaterThanEqual(BigDecimal grade);
    
    boolean existsByStudentIdAndSubjectId(Long studentId, Long subjectId);
    
    long countBySubjectId(Long subjectId);
    
    long countByStatus(EnrollmentStatus status);
}