package br.edu.infnet.classroomapi.application.mappers;

import br.edu.infnet.classroomapi.application.dto.response.EnrollmentResponseDTO;
import br.edu.infnet.classroomapi.domain.entities.Enrollment;
import br.edu.infnet.classroomapi.infrastructure.persistence.repositories.EnrollmentJpaRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentDTOMapper.class, SubjectDTOMapper.class})
public interface EnrollmentDTOMapper {

    @Mapping(target = "approved", source = "approved")
    @Mapping(target = "subject", source = "subject")
    EnrollmentResponseDTO toResponseDTO(Enrollment enrollment, @Context EnrollmentJpaRepository enrollmentRepository);

    List<EnrollmentResponseDTO> toResponseDTOList(List<Enrollment> enrollments, @Context EnrollmentJpaRepository enrollmentRepository);
}