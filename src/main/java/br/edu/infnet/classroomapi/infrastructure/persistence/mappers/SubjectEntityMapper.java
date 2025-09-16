package br.edu.infnet.classroomapi.infrastructure.persistence.mappers;

import br.edu.infnet.classroomapi.domain.entities.Subject;
import br.edu.infnet.classroomapi.infrastructure.persistence.entities.SubjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectEntityMapper {
    
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "professor", ignore = true)
    SubjectEntity toEntity(Subject subject);
    
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "professor", ignore = true)
    Subject toDomain(SubjectEntity entity);
    
    List<Subject> toDomainList(List<SubjectEntity> entities);
    
    List<SubjectEntity> toEntityList(List<Subject> subjects);
}