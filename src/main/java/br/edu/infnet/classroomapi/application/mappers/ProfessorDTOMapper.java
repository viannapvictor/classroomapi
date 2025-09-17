package br.edu.infnet.classroomapi.application.mappers;

import br.edu.infnet.classroomapi.application.dto.request.CreateProfessorRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.ProfessorResponseDTO;
import br.edu.infnet.classroomapi.domain.entities.Professor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfessorDTOMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    Professor toDomain(CreateProfessorRequestDTO dto);

    ProfessorResponseDTO toResponseDTO(Professor professor);

    List<ProfessorResponseDTO> toResponseDTOList(List<Professor> professors);
}