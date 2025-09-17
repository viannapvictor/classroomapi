package br.edu.infnet.classroomapi.application.mappers;

import br.edu.infnet.classroomapi.application.dto.response.EnrollmentResponseDTO;
import br.edu.infnet.classroomapi.domain.entities.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentDTOMapper.class, SubjectDTOMapper.class})
public interface EnrollmentDTOMapper {

    @Mapping(target = "approved", source = "approved")
    EnrollmentResponseDTO toResponseDTO(Enrollment enrollment);

    List<EnrollmentResponseDTO> toResponseDTOList(List<Enrollment> enrollments);
}