package br.edu.infnet.classroomapi.application.mappers;

import br.edu.infnet.classroomapi.application.dto.request.CreateStudentRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentSummaryDTO;
import br.edu.infnet.classroomapi.domain.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AddressDTOMapper.class})
public interface StudentDTOMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    Student toDomain(CreateStudentRequestDTO dto);

    StudentResponseDTO toResponseDTO(Student student);

    StudentSummaryDTO toSummaryDTO(Student student);

    List<StudentResponseDTO> toResponseDTOList(List<Student> students);

    List<StudentSummaryDTO> toSummaryDTOList(List<Student> students);
}