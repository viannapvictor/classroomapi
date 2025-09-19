package br.edu.infnet.classroomapi.infrastructure.persistence.mappers;

import br.edu.infnet.classroomapi.domain.entities.Address;
import br.edu.infnet.classroomapi.domain.entities.Student;
import br.edu.infnet.classroomapi.infrastructure.persistence.entities.AddressEntity;
import br.edu.infnet.classroomapi.infrastructure.persistence.entities.StudentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("StudentEntityMapper Tests")
class StudentEntityMapperTest {

    @Autowired
    private StudentEntityMapper mapper;

    private Student domainStudent;
    private StudentEntity entityStudent;
    private Address domainAddress;
    private AddressEntity entityAddress;

    @BeforeEach
    void setUp() {
        domainAddress = new Address(
                "Rua das Flores",
                "123",
                "Apto 45",
                "Centro",
                "São Paulo",
                "SP",
                "01234-567",
                "Brasil"
        );

        entityAddress = new AddressEntity(
                "Rua das Flores",
                "123",
                "Apto 45",
                "Centro",
                "São Paulo",
                "SP",
                "01234-567",
                "Brasil"
        );

        domainStudent = new Student();
        domainStudent.setId(1L);
        domainStudent.setName("João Silva");
        domainStudent.setCpf("12345678901");
        domainStudent.setEmail("joao@email.com");
        domainStudent.setPhone("(11) 99999-9999");
        domainStudent.setAddress(domainAddress);
        domainStudent.setCreatedAt(LocalDateTime.now());
        domainStudent.setUpdatedAt(LocalDateTime.now());
        domainStudent.setEnrollments(new ArrayList<>());

        entityStudent = new StudentEntity();
        entityStudent.setId(1L);
        entityStudent.setName("João Silva");
        entityStudent.setCpf("12345678901");
        entityStudent.setEmail("joao@email.com");
        entityStudent.setPhone("(11) 99999-9999");
        entityStudent.setAddress(entityAddress);
        entityStudent.setCreatedAt(LocalDateTime.now());
        entityStudent.setUpdatedAt(LocalDateTime.now());
        entityStudent.setEnrollments(new ArrayList<>());
    }

    @Nested
    @DisplayName("Domain to Entity Mapping Tests")
    class DomainToEntityTests {

        @Test
        @DisplayName("Should map domain student to entity successfully")
        void shouldMapDomainStudentToEntitySuccessfully() {
            StudentEntity result = mapper.toEntity(domainStudent);

            assertNotNull(result);
            assertEquals(domainStudent.getId(), result.getId());
            assertEquals(domainStudent.getName(), result.getName());
            assertEquals(domainStudent.getCpf(), result.getCpf());
            assertEquals(domainStudent.getEmail(), result.getEmail());
            assertEquals(domainStudent.getPhone(), result.getPhone());
            assertEquals(domainStudent.getCreatedAt(), result.getCreatedAt());
            assertEquals(domainStudent.getUpdatedAt(), result.getUpdatedAt());

            assertNotNull(result.getAddress());
            assertEquals(domainStudent.getAddress().getStreet(), result.getAddress().getStreet());
            assertEquals(domainStudent.getAddress().getNumber(), result.getAddress().getNumber());
            assertEquals(domainStudent.getAddress().getComplement(), result.getAddress().getComplement());
            assertEquals(domainStudent.getAddress().getNeighborhood(), result.getAddress().getNeighborhood());
            assertEquals(domainStudent.getAddress().getCity(), result.getAddress().getCity());
            assertEquals(domainStudent.getAddress().getState(), result.getAddress().getState());
            assertEquals(domainStudent.getAddress().getZipCode(), result.getAddress().getZipCode());
            assertEquals(domainStudent.getAddress().getCountry(), result.getAddress().getCountry());

            assertNotNull(result.getEnrollments());
            assertTrue(result.getEnrollments().isEmpty());
        }

        @Test
        @DisplayName("Should handle null domain student")
        void shouldHandleNullDomainStudent() {
            StudentEntity result = mapper.toEntity(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Should handle domain student with null address")
        void shouldHandleDomainStudentWithNullAddress() {
            domainStudent.setAddress(null);

            StudentEntity result = mapper.toEntity(domainStudent);

            assertNotNull(result);
            assertEquals(domainStudent.getName(), result.getName());
            assertNull(result.getAddress());
        }

        @Test
        @DisplayName("Should map list of domain students to entities")
        void shouldMapListOfDomainStudentsToEntities() {
            Student student2 = new Student();
            student2.setId(2L);
            student2.setName("Maria Santos");
            student2.setCpf("98765432109");
            student2.setEmail("maria@email.com");
            student2.setAddress(domainAddress);

            List<Student> domainList = Arrays.asList(domainStudent, student2);

            List<StudentEntity> result = mapper.toEntityList(domainList);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("João Silva", result.get(0).getName());
            assertEquals("Maria Santos", result.get(1).getName());
        }

        @Test
        @DisplayName("Should handle empty list for domain to entity")
        void shouldHandleEmptyListForDomainToEntity() {
            List<StudentEntity> result = mapper.toEntityList(new ArrayList<>());

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should handle null list for domain to entity")
        void shouldHandleNullListForDomainToEntity() {
            List<StudentEntity> result = mapper.toEntityList(null);
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Entity to Domain Mapping Tests")
    class EntityToDomainTests {

        @Test
        @DisplayName("Should map entity student to domain successfully")
        void shouldMapEntityStudentToDomainSuccessfully() {
            Student result = mapper.toDomain(entityStudent);

            assertNotNull(result);
            assertEquals(entityStudent.getId(), result.getId());
            assertEquals(entityStudent.getName(), result.getName());
            assertEquals(entityStudent.getCpf(), result.getCpf());
            assertEquals(entityStudent.getEmail(), result.getEmail());
            assertEquals(entityStudent.getPhone(), result.getPhone());
            assertEquals(entityStudent.getCreatedAt(), result.getCreatedAt());
            assertEquals(entityStudent.getUpdatedAt(), result.getUpdatedAt());

            assertNotNull(result.getAddress());
            assertEquals(entityStudent.getAddress().getStreet(), result.getAddress().getStreet());
            assertEquals(entityStudent.getAddress().getNumber(), result.getAddress().getNumber());
            assertEquals(entityStudent.getAddress().getComplement(), result.getAddress().getComplement());
            assertEquals(entityStudent.getAddress().getNeighborhood(), result.getAddress().getNeighborhood());
            assertEquals(entityStudent.getAddress().getCity(), result.getAddress().getCity());
            assertEquals(entityStudent.getAddress().getState(), result.getAddress().getState());
            assertEquals(entityStudent.getAddress().getZipCode(), result.getAddress().getZipCode());
            assertEquals(entityStudent.getAddress().getCountry(), result.getAddress().getCountry());

            assertNotNull(result.getEnrollments());
            assertTrue(result.getEnrollments().isEmpty());
        }

        @Test
        @DisplayName("Should handle null entity student")
        void shouldHandleNullEntityStudent() {
            Student result = mapper.toDomain(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Should handle entity student with null address")
        void shouldHandleEntityStudentWithNullAddress() {
            entityStudent.setAddress(null);

            Student result = mapper.toDomain(entityStudent);

            assertNotNull(result);
            assertEquals(entityStudent.getName(), result.getName());
            assertNull(result.getAddress());
            assertNotNull(result.getEnrollments());
            assertTrue(result.getEnrollments().isEmpty());
        }

        @Test
        @DisplayName("Should map list of entity students to domain")
        void shouldMapListOfEntityStudentsToDomain() {
            StudentEntity entity2 = new StudentEntity();
            entity2.setId(2L);
            entity2.setName("Maria Santos");
            entity2.setCpf("98765432109");
            entity2.setEmail("maria@email.com");
            entity2.setAddress(entityAddress);

            List<StudentEntity> entityList = Arrays.asList(entityStudent, entity2);

            List<Student> result = mapper.toDomainList(entityList);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("João Silva", result.get(0).getName());
            assertEquals("Maria Santos", result.get(1).getName());

            result.forEach(student -> {
                assertNotNull(student.getEnrollments());
                assertTrue(student.getEnrollments().isEmpty());
            });
        }

        @Test
        @DisplayName("Should handle empty list for entity to domain")
        void shouldHandleEmptyListForEntityToDomain() {
            List<Student> result = mapper.toDomainList(new ArrayList<>());

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should handle null list for entity to domain")
        void shouldHandleNullListForEntityToDomain() {
            List<Student> result = mapper.toDomainList(null);
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("AfterMapping Tests")
    class AfterMappingTests {

        @Test
        @DisplayName("Should initialize enrollments when null")
        void shouldInitializeEnrollmentsWhenNull() {
            entityStudent.setEnrollments(null);

            Student result = mapper.toDomain(entityStudent);

            assertNotNull(result.getEnrollments());
            assertTrue(result.getEnrollments().isEmpty());
        }

        @Test
        @DisplayName("Should not override existing enrollments list")
        void shouldNotOverrideExistingEnrollmentsList() {
            Student result = mapper.toDomain(entityStudent);

            assertNotNull(result.getEnrollments());
            assertTrue(result.getEnrollments() instanceof ArrayList);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle student with all null optional fields")
        void shouldHandleStudentWithAllNullOptionalFields() {
            domainStudent.setPhone(null);
            domainStudent.setAddress(null);

            StudentEntity result = mapper.toEntity(domainStudent);

            assertNotNull(result);
            assertEquals(domainStudent.getName(), result.getName());
            assertEquals(domainStudent.getCpf(), result.getCpf());
            assertEquals(domainStudent.getEmail(), result.getEmail());
            assertNull(result.getPhone());
            assertNull(result.getAddress());
        }

        @Test
        @DisplayName("Should handle entity with all null optional fields")
        void shouldHandleEntityWithAllNullOptionalFields() {
            entityStudent.setPhone(null);
            entityStudent.setAddress(null);

            Student result = mapper.toDomain(entityStudent);

            assertNotNull(result);
            assertEquals(entityStudent.getName(), result.getName());
            assertEquals(entityStudent.getCpf(), result.getCpf());
            assertEquals(entityStudent.getEmail(), result.getEmail());
            assertNull(result.getPhone());
            assertNull(result.getAddress());
            assertNotNull(result.getEnrollments());
        }

        @Test
        @DisplayName("Should preserve all timestamp fields")
        void shouldPreserveAllTimestampFields() {
            LocalDateTime specificTime = LocalDateTime.of(2023, 1, 15, 10, 30, 0);
            domainStudent.setCreatedAt(specificTime);
            domainStudent.setUpdatedAt(specificTime.plusHours(1));

            StudentEntity entityResult = mapper.toEntity(domainStudent);
            Student domainResult = mapper.toDomain(entityResult);

            assertEquals(specificTime, entityResult.getCreatedAt());
            assertEquals(specificTime.plusHours(1), entityResult.getUpdatedAt());
            assertEquals(specificTime, domainResult.getCreatedAt());
            assertEquals(specificTime.plusHours(1), domainResult.getUpdatedAt());
        }
    }
}
