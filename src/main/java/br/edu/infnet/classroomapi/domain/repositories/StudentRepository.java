package br.edu.infnet.classroomapi.domain.repositories;

import br.edu.infnet.classroomapi.domain.entities.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    
    Student save(Student student);
    
    Optional<Student> findById(Long id);
    
    Optional<Student> findByCpf(String cpf);
    
    Optional<Student> findByEmail(String email);
    
    List<Student> findAll();
    
    List<Student> findByNameContainingIgnoreCase(String name);
    
    List<Student> findBySubjectId(Long subjectId);
    
    boolean existsByCpf(String cpf);
    
    boolean existsByEmail(String email);
    
    void deleteById(Long id);
    
    long count();
}