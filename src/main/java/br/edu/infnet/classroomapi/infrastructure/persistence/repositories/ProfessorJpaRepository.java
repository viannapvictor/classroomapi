package br.edu.infnet.classroomapi.infrastructure.persistence.repositories;

import br.edu.infnet.classroomapi.infrastructure.persistence.entities.ProfessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorJpaRepository extends JpaRepository<ProfessorEntity, Long> {
    
    Optional<ProfessorEntity> findByEmail(String email);
    
    @Query("SELECT p FROM ProfessorEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ProfessorEntity> findByNameContainingIgnoreCase(@Param("name") String name);
    
    boolean existsByEmail(String email);
}