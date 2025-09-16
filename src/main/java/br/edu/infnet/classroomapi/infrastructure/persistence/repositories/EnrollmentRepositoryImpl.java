package br.edu.infnet.classroomapi.infrastructure.persistence.repositories;

import br.edu.infnet.classroomapi.domain.entities.Enrollment;
import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
import br.edu.infnet.classroomapi.domain.repositories.EnrollmentRepository;
import br.edu.infnet.classroomapi.infrastructure.persistence.mappers.EnrollmentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EnrollmentRepositoryImpl implements EnrollmentRepository {
    
    private final EnrollmentJpaRepository jpaRepository;
    private final EnrollmentEntityMapper mapper;
    
    @Override
    public Enrollment save(Enrollment enrollment) {
        var entity = mapper.toEntity(enrollment);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Enrollment> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Enrollment> findByStudentIdAndSubjectId(Long studentId, Long subjectId) {
        return jpaRepository.findByStudentIdAndSubjectId(studentId, subjectId)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Enrollment> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }
    
    @Override
    public List<Enrollment> findByStudentId(Long studentId) {
        return mapper.toDomainList(jpaRepository.findByStudentId(studentId));
    }
    
    @Override
    public List<Enrollment> findBySubjectId(Long subjectId) {
        return mapper.toDomainList(jpaRepository.findBySubjectId(subjectId));
    }
    
    @Override
    public List<Enrollment> findByStatus(EnrollmentStatus status) {
        return mapper.toDomainList(jpaRepository.findByStatus(status));
    }
    
    @Override
    public List<Enrollment> findBySubjectIdAndStatus(Long subjectId, EnrollmentStatus status) {
        return mapper.toDomainList(jpaRepository.findBySubjectIdAndStatus(subjectId, status));
    }
    
    @Override
    public List<Enrollment> findApprovedBySubjectId(Long subjectId) {
        return mapper.toDomainList(jpaRepository.findApprovedBySubjectId(subjectId));
    }
    
    @Override
    public List<Enrollment> findReprobedBySubjectId(Long subjectId) {
        return mapper.toDomainList(jpaRepository.findReprobedBySubjectId(subjectId));
    }
    
    @Override
    public List<Enrollment> findByGradeGreaterThanEqual(BigDecimal grade) {
        return mapper.toDomainList(jpaRepository.findByGradeGreaterThanEqual(grade));
    }
    
    @Override
    public boolean existsByStudentIdAndSubjectId(Long studentId, Long subjectId) {
        return jpaRepository.existsByStudentIdAndSubjectId(studentId, subjectId);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public long count() {
        return jpaRepository.count();
    }
    
    @Override
    public long countBySubjectId(Long subjectId) {
        return jpaRepository.countBySubjectId(subjectId);
    }
    
    @Override
    public long countByStatus(EnrollmentStatus status) {
        return jpaRepository.countByStatus(status);
    }
}