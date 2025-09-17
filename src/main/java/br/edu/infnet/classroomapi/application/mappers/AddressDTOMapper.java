package br.edu.infnet.classroomapi.application.mappers;

import br.edu.infnet.classroomapi.application.dto.request.AddressRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.AddressResponseDTO;
import br.edu.infnet.classroomapi.domain.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressDTOMapper {

    Address toDomain(AddressRequestDTO dto);

    @Mapping(target = "fullAddress", source = "fullAddress")
    AddressResponseDTO toResponseDTO(Address address);
}