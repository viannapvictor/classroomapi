package br.edu.infnet.classroomapi.infrastructure.persistence.mappers;

import br.edu.infnet.classroomapi.domain.entities.Subject;
import br.edu.infnet.classroomapi.infrastructure.persistence.entities.SubjectEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ProfessorEntityMapper.class})
public interface SubjectEntityMapper {

    @Mapping(target = "enrollments", ignore = true)
    SubjectEntity toEntity(Subject subject);

    @Mapping(target = "enrollments", ignore = true)
    Subject toDomain(SubjectEntity entity);

    @AfterMapping
    default void initializeEnrollments(@MappingTarget Subject subject) {
        if (subject.getEnrollments() == null) {
            subject.setEnrollments(new ArrayList<>());
        }
    }

    List<Subject> toDomainList(List<SubjectEntity> entities);

    List<SubjectEntity> toEntityList(List<Subject> subjects);
}