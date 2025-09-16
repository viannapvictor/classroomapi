package br.edu.infnet.classroomapi.infrastructure.persistence.repositories;

import br.edu.infnet.classroomapi.domain.entities.Professor;
import br.edu.infnet.classroomapi.domain.repositories.ProfessorRepository;
import br.edu.infnet.classroomapi.infrastructure.persistence.mappers.ProfessorEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfessorRepositoryImpl implements ProfessorRepository {
    
    private final ProfessorJpaRepository jpaRepository;
    private final ProfessorEntityMapper mapper;
    
    @Override
    public Professor save(Professor professor) {
        var entity = mapper.toEntity(professor);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Professor> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Professor> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Professor> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }
    
    @Override
    public List<Professor> findByNameContainingIgnoreCase(String name) {
        return mapper.toDomainList(jpaRepository.findByNameContainingIgnoreCase(name));
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
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