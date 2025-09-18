package br.edu.infnet.classroomapi.application.mappers;

import br.edu.infnet.classroomapi.application.dto.request.CreateSubjectRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.SubjectSummaryDTO;
import br.edu.infnet.classroomapi.domain.entities.Subject;
import br.edu.infnet.classroomapi.infrastructure.persistence.repositories.EnrollmentJpaRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProfessorDTOMapper.class})
public interface SubjectDTOMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    Subject toDomain(CreateSubjectRequestDTO dto);

    @Mapping(target = "enrolledStudentsCount", expression = "java(getEnrolledStudentsCount(subject, enrollmentRepository))")
    SubjectResponseDTO toResponseDTO(Subject subject, @Context EnrollmentJpaRepository enrollmentRepository);

    default Long getEnrolledStudentsCount(Subject subject, EnrollmentJpaRepository enrollmentRepository) {
        if (subject.getId() != null) {
            return enrollmentRepository.countBySubjectId(subject.getId());
        }
        return 0L;
    }

    SubjectSummaryDTO toSummaryDTO(Subject subject);

    List<SubjectResponseDTO> toResponseDTOList(List<Subject> subjects, @Context EnrollmentJpaRepository enrollmentRepository);

    List<SubjectSummaryDTO> toSummaryDTOList(List<Subject> subjects);
}