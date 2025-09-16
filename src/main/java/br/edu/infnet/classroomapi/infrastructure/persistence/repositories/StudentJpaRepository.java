package br.edu.infnet.classroomapi.infrastructure.persistence.repositories;

import br.edu.infnet.classroomapi.infrastructure.persistence.entities.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentJpaRepository extends JpaRepository<StudentEntity, Long> {
    
    Optional<StudentEntity> findByCpf(String cpf);
    
    Optional<StudentEntity> findByEmail(String email);
    
    @Query("SELECT s FROM StudentEntity s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<StudentEntity> findByNameContainingIgnoreCase(@Param("name") String name);
    
    @Query("SELECT DISTINCT s FROM StudentEntity s " +
           "JOIN s.enrollments e " +
           "WHERE e.subject.id = :subjectId")
    List<StudentEntity> findBySubjectId(@Param("subjectId") Long subjectId);
    
    boolean existsByCpf(String cpf);
    
    boolean existsByEmail(String email);
}