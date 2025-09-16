package br.edu.infnet.classroomapi.domain.repositories;

import br.edu.infnet.classroomapi.domain.entities.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository {
    
    Subject save(Subject subject);
    
    Optional<Subject> findById(Long id);
    
    Optional<Subject> findByCode(String code);
    
    List<Subject> findAll();
    
    List<Subject> findByNameContainingIgnoreCase(String name);
    
    List<Subject> findByProfessorId(Long professorId);
    
    boolean existsByCode(String code);
    
    void deleteById(Long id);
    
    long count();
}