package br.edu.infnet.classroomapi.infrastructure.persistence.repositories;

import br.edu.infnet.classroomapi.infrastructure.persistence.entities.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectJpaRepository extends JpaRepository<SubjectEntity, Long> {
    
    Optional<SubjectEntity> findByCode(String code);
    
    @Query("SELECT s FROM SubjectEntity s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<SubjectEntity> findByNameContainingIgnoreCase(@Param("name") String name);
    
    List<SubjectEntity> findByProfessorId(Long professorId);
    
    boolean existsByCode(String code);
}