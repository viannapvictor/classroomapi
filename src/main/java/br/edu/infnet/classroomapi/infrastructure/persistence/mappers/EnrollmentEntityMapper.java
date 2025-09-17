package br.edu.infnet.classroomapi.infrastructure.persistence.mappers;

import br.edu.infnet.classroomapi.domain.entities.Enrollment;
import br.edu.infnet.classroomapi.infrastructure.persistence.entities.EnrollmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentEntityMapper.class, SubjectEntityMapper.class})
public interface EnrollmentEntityMapper {

    EnrollmentEntity toEntity(Enrollment enrollment);

    Enrollment toDomain(EnrollmentEntity entity);
    
    List<Enrollment> toDomainList(List<EnrollmentEntity> entities);
    
    List<EnrollmentEntity> toEntityList(List<Enrollment> enrollments);
}