package br.edu.infnet.classroomapi.domain.repositories;

import br.edu.infnet.classroomapi.domain.entities.Professor;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepository {
    
    Professor save(Professor professor);
    
    Optional<Professor> findById(Long id);
    
    Optional<Professor> findByEmail(String email);
    
    List<Professor> findAll();
    
    List<Professor> findByNameContainingIgnoreCase(String name);
    
    boolean existsByEmail(String email);
    
    void deleteById(Long id);
    
    long count();
}