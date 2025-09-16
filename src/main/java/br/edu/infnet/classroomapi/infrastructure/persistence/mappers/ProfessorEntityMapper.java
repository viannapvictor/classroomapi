package br.edu.infnet.classroomapi.infrastructure.persistence.mappers;

import br.edu.infnet.classroomapi.domain.entities.Professor;
import br.edu.infnet.classroomapi.infrastructure.persistence.entities.ProfessorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfessorEntityMapper {
    
    @Mapping(target = "subjects", ignore = true)
    ProfessorEntity toEntity(Professor professor);
    
    @Mapping(target = "subjects", ignore = true)
    Professor toDomain(ProfessorEntity entity);
    
    List<Professor> toDomainList(List<ProfessorEntity> entities);
    
    List<ProfessorEntity> toEntityList(List<Professor> professors);
}