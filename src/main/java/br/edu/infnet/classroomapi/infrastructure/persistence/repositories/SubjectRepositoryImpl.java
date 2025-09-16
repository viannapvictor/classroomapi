package br.edu.infnet.classroomapi.infrastructure.persistence.repositories;

import br.edu.infnet.classroomapi.domain.entities.Subject;
import br.edu.infnet.classroomapi.domain.repositories.SubjectRepository;
import br.edu.infnet.classroomapi.infrastructure.persistence.mappers.SubjectEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubjectRepositoryImpl implements SubjectRepository {
    
    private final SubjectJpaRepository jpaRepository;
    private final SubjectEntityMapper mapper;
    
    @Override
    public Subject save(Subject subject) {
        var entity = mapper.toEntity(subject);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Subject> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Subject> findByCode(String code) {
        return jpaRepository.findByCode(code)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Subject> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }
    
    @Override
    public List<Subject> findByNameContainingIgnoreCase(String name) {
        return mapper.toDomainList(jpaRepository.findByNameContainingIgnoreCase(name));
    }
    
    @Override
    public List<Subject> findByProfessorId(Long professorId) {
        return mapper.toDomainList(jpaRepository.findByProfessorId(professorId));
    }
    
    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public long count() {
        return jpaRepository.count();
    }
}