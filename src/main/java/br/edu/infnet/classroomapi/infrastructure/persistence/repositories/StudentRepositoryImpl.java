package br.edu.infnet.classroomapi.infrastructure.persistence.repositories;

import br.edu.infnet.classroomapi.domain.entities.Student;
import br.edu.infnet.classroomapi.domain.repositories.StudentRepository;
import br.edu.infnet.classroomapi.infrastructure.persistence.mappers.StudentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {
    
    private final StudentJpaRepository jpaRepository;
    private final StudentEntityMapper mapper;
    
    @Override
    public Student save(Student student) {
        var entity = mapper.toEntity(student);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Student> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Student> findByCpf(String cpf) {
        return jpaRepository.findByCpf(cpf)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Student> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Student> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }
    
    @Override
    public List<Student> findByNameContainingIgnoreCase(String name) {
        return mapper.toDomainList(jpaRepository.findByNameContainingIgnoreCase(name));
    }
    
    @Override
    public List<Student> findBySubjectId(Long subjectId) {
        return mapper.toDomainList(jpaRepository.findBySubjectId(subjectId));
    }
    
    @Override
    public boolean existsByCpf(String cpf) {
        return jpaRepository.existsByCpf(cpf);
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