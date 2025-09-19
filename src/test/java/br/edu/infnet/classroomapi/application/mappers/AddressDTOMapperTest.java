package br.edu.infnet.classroomapi.application.mappers;

import br.edu.infnet.classroomapi.application.dto.request.AddressRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.AddressResponseDTO;
import br.edu.infnet.classroomapi.domain.entities.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("AddressDTOMapper Tests")
class AddressDTOMapperTest {

    private AddressDTOMapper mapper;
    private AddressRequestDTO addressRequestDTO;
    private Address address;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AddressDTOMapper.class);

        addressRequestDTO = new AddressRequestDTO(
            "Rua das Flores", "123", "Apto 45", "Centro",
            "São Paulo", "SP", "01234-567", "Brasil"
        );

        address = new Address(
            "Rua das Flores", "123", "Apto 45", "Centro",
            "São Paulo", "SP", "01234-567", "Brasil"
        );
    }

    @Nested
    @DisplayName("Domain Mapping Tests")
    class DomainMappingTests {

        @Test
        @DisplayName("Should map AddressRequestDTO to domain Address successfully")
        void shouldMapAddressRequestDTOToDomainSuccessfully() {
            Address result = mapper.toDomain(addressRequestDTO);

            assertNotNull(result);
            assertEquals(addressRequestDTO.getStreet(), result.getStreet());
            assertEquals(addressRequestDTO.getNumber(), result.getNumber());
            assertEquals(addressRequestDTO.getComplement(), result.getComplement());
            assertEquals(addressRequestDTO.getNeighborhood(), result.getNeighborhood());
            assertEquals(addressRequestDTO.getCity(), result.getCity());
            assertEquals(addressRequestDTO.getState(), result.getState());
            assertEquals(addressRequestDTO.getZipCode(), result.getZipCode());
            assertEquals(addressRequestDTO.getCountry(), result.getCountry());
        }

        @Test
        @DisplayName("Should handle null AddressRequestDTO")
        void shouldHandleNullAddressRequestDTO() {
            Address result = mapper.toDomain(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Should map AddressRequestDTO with null fields")
        void shouldMapAddressRequestDTOWithNullFields() {
            AddressRequestDTO dtoWithNulls = new AddressRequestDTO(
                null, "123", null, "Centro",
                "São Paulo", null, "01234-567", null
            );

            Address result = mapper.toDomain(dtoWithNulls);

            assertNotNull(result);
            assertNull(result.getStreet());
            assertEquals("123", result.getNumber());
            assertNull(result.getComplement());
            assertEquals("Centro", result.getNeighborhood());
            assertEquals("São Paulo", result.getCity());
            assertNull(result.getState());
            assertEquals("01234-567", result.getZipCode());
            assertNull(result.getCountry());
        }
    }

    @Nested
    @DisplayName("Response DTO Mapping Tests")
    class ResponseDTOMappingTests {

        @Test
        @DisplayName("Should map domain Address to AddressResponseDTO successfully")
        void shouldMapDomainAddressToResponseDTOSuccessfully() {
            AddressResponseDTO result = mapper.toResponseDTO(address);

            assertNotNull(result);
            assertEquals(address.getStreet(), result.getStreet());
            assertEquals(address.getNumber(), result.getNumber());
            assertEquals(address.getComplement(), result.getComplement());
            assertEquals(address.getNeighborhood(), result.getNeighborhood());
            assertEquals(address.getCity(), result.getCity());
            assertEquals(address.getState(), result.getState());
            assertEquals(address.getZipCode(), result.getZipCode());
            assertEquals(address.getCountry(), result.getCountry());
            assertEquals(address.getFullAddress(), result.getFullAddress());
        }

        @Test
        @DisplayName("Should handle null domain Address")
        void shouldHandleNullDomainAddress() {
            AddressResponseDTO result = mapper.toResponseDTO(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Should map domain Address with null fields")
        void shouldMapDomainAddressWithNullFields() {
            Address addressWithNulls = new Address(
                null, "123", null, "Centro",
                "São Paulo", null, "01234-567", null
            );

            AddressResponseDTO result = mapper.toResponseDTO(addressWithNulls);

            assertNotNull(result);
            assertNull(result.getStreet());
            assertEquals("123", result.getNumber());
            assertNull(result.getComplement());
            assertEquals("Centro", result.getNeighborhood());
            assertEquals("São Paulo", result.getCity());
            assertNull(result.getState());
            assertEquals("01234-567", result.getZipCode());
            assertNull(result.getCountry());
            assertEquals(addressWithNulls.getFullAddress(), result.getFullAddress());
        }
    }
}