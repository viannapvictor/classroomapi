package br.edu.infnet.classroomapi.infrastructure.persistence.mappers;

import br.edu.infnet.classroomapi.domain.entities.Student;
import br.edu.infnet.classroomapi.infrastructure.persistence.entities.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentEntityMapper {
    
    @Mapping(target = "enrollments", ignore = true)
    StudentEntity toEntity(Student student);
    
    @Mapping(target = "enrollments", ignore = true)
    Student toDomain(StudentEntity entity);
    
    List<Student> toDomainList(List<StudentEntity> entities);
    
    List<StudentEntity> toEntityList(List<Student> students);
}