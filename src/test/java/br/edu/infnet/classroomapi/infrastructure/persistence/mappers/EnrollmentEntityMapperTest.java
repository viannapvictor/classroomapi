package br.edu.infnet.classroomapi.infrastructure.persistence.mappers;

import br.edu.infnet.classroomapi.domain.entities.*;
import br.edu.infnet.classroomapi.domain.enums.EnrollmentStatus;
import br.edu.infnet.classroomapi.domain.enums.UserRole;
import br.edu.infnet.classroomapi.infrastructure.persistence.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("EnrollmentEntityMapper Tests")
class EnrollmentEntityMapperTest {

    @Autowired
    private EnrollmentEntityMapper mapper;

    private Enrollment domainEnrollment;
    private EnrollmentEntity entityEnrollment;
    private Student domainStudent;
    private StudentEntity entityStudent;
    private Subject domainSubject;
    private SubjectEntity entitySubject;
    private Professor domainProfessor;
    private ProfessorEntity entityProfessor;

    @BeforeEach
    void setUp() {
        domainProfessor = new Professor();
        domainProfessor.setId(1L);
        domainProfessor.setName("Prof. Silva");
        domainProfessor.setEmail("prof@email.com");
        domainProfessor.setRole(UserRole.PROFESSOR);

        entityProfessor = new ProfessorEntity();
        entityProfessor.setId(1L);
        entityProfessor.setName("Prof. Silva");
        entityProfessor.setEmail("prof@email.com");
        entityProfessor.setRole(UserRole.PROFESSOR);

        Address domainAddress = new Address("Rua A", "123", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");
        AddressEntity entityAddress = new AddressEntity("Rua A", "123", null, "Centro", "São Paulo", "SP", "01234-567", "Brasil");

        domainStudent = new Student();
        domainStudent.setId(1L);
        domainStudent.setName("João Silva");
        domainStudent.setCpf("12345678901");
        domainStudent.setEmail("joao@email.com");
        domainStudent.setAddress(domainAddress);
        domainStudent.setEnrollments(new ArrayList<>());

        entityStudent = new StudentEntity();
        entityStudent.setId(1L);
        entityStudent.setName("João Silva");
        entityStudent.setCpf("12345678901");
        entityStudent.setEmail("joao@email.com");
        entityStudent.setAddress(entityAddress);
        entityStudent.setEnrollments(new ArrayList<>());

        domainSubject = new Subject();
        domainSubject.setId(1L);
        domainSubject.setName("Java Programming");
        domainSubject.setCode("JAVA101");
        domainSubject.setProfessor(domainProfessor);
        domainSubject.setEnrollments(new ArrayList<>());

        entitySubject = new SubjectEntity();
        entitySubject.setId(1L);
        entitySubject.setName("Java Programming");
        entitySubject.setCode("JAVA101");
        entitySubject.setProfessor(entityProfessor);
        entitySubject.setEnrollments(new ArrayList<>());

        LocalDateTime now = LocalDateTime.now();
        
        domainEnrollment = new Enrollment();
        domainEnrollment.setId(1L);
        domainEnrollment.setStudent(domainStudent);
        domainEnrollment.setSubject(domainSubject);
        domainEnrollment.setStatus(EnrollmentStatus.ACTIVE);
        domainEnrollment.setGrade(BigDecimal.valueOf(8.5));
        domainEnrollment.setEnrollmentDate(now);
        domainEnrollment.setCreatedAt(now);
        domainEnrollment.setUpdatedAt(now);

        entityEnrollment = new EnrollmentEntity();
        entityEnrollment.setId(1L);
        entityEnrollment.setStudent(entityStudent);
        entityEnrollment.setSubject(entitySubject);
        entityEnrollment.setStatus(EnrollmentStatus.ACTIVE);
        entityEnrollment.setGrade(BigDecimal.valueOf(8.5));
        entityEnrollment.setEnrollmentDate(now);
        entityEnrollment.setCreatedAt(now);
        entityEnrollment.setUpdatedAt(now);
    }

    @Nested
    @DisplayName("Domain to Entity Mapping Tests")
    class DomainToEntityTests {

        @Test
        @DisplayName("Should map domain enrollment to entity successfully")
        void shouldMapDomainEnrollmentToEntitySuccessfully() {
            EnrollmentEntity result = mapper.toEntity(domainEnrollment);

            assertNotNull(result);
            assertEquals(domainEnrollment.getId(), result.getId());
            assertEquals(domainEnrollment.getStatus(), result.getStatus());
            assertEquals(domainEnrollment.getGrade(), result.getGrade());
            assertEquals(domainEnrollment.getEnrollmentDate(), result.getEnrollmentDate());
            assertEquals(domainEnrollment.getCreatedAt(), result.getCreatedAt());
            assertEquals(domainEnrollment.getUpdatedAt(), result.getUpdatedAt());

            assertNotNull(result.getStudent());
            assertEquals(domainEnrollment.getStudent().getId(), result.getStudent().getId());
            assertEquals(domainEnrollment.getStudent().getName(), result.getStudent().getName());
            assertEquals(domainEnrollment.getStudent().getCpf(), result.getStudent().getCpf());

            assertNotNull(result.getSubject());
            assertEquals(domainEnrollment.getSubject().getId(), result.getSubject().getId());
            assertEquals(domainEnrollment.getSubject().getName(), result.getSubject().getName());
            assertEquals(domainEnrollment.getSubject().getCode(), result.getSubject().getCode());
        }

        @Test
        @DisplayName("Should handle null domain enrollment")
        void shouldHandleNullDomainEnrollment() {
            EnrollmentEntity result = mapper.toEntity(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Should handle domain enrollment with null grade")
        void shouldHandleDomainEnrollmentWithNullGrade() {
            domainEnrollment.setGrade(null);

            EnrollmentEntity result = mapper.toEntity(domainEnrollment);

            assertNotNull(result);
            assertNull(result.getGrade());
            assertEquals(domainEnrollment.getStatus(), result.getStatus());
        }

        @Test
        @DisplayName("Should handle domain enrollment with null completion date")
        void shouldHandleDomainEnrollmentWithNullCompletionDate() {
            domainEnrollment.setCompletionDate(null);

            EnrollmentEntity result = mapper.toEntity(domainEnrollment);

            assertNotNull(result);
            assertNull(result.getCompletionDate());
        }

        @Test
        @DisplayName("Should map list of domain enrollments to entities")
        void shouldMapListOfDomainEnrollmentsToEntities() {
            Enrollment enrollment2 = new Enrollment();
            enrollment2.setId(2L);
            enrollment2.setStudent(domainStudent);
            enrollment2.setSubject(domainSubject);
            enrollment2.setStatus(EnrollmentStatus.COMPLETED);
            enrollment2.setGrade(BigDecimal.valueOf(9.0));

            List<Enrollment> domainList = Arrays.asList(domainEnrollment, enrollment2);

            List<EnrollmentEntity> result = mapper.toEntityList(domainList);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(domainEnrollment.getId(), result.get(0).getId());
            assertEquals(enrollment2.getId(), result.get(1).getId());
            assertEquals(EnrollmentStatus.ACTIVE, result.get(0).getStatus());
            assertEquals(EnrollmentStatus.COMPLETED, result.get(1).getStatus());
        }
    }

    @Nested
    @DisplayName("Entity to Domain Mapping Tests")
    class EntityToDomainTests {

        @Test
        @DisplayName("Should map entity enrollment to domain successfully")
        void shouldMapEntityEnrollmentToDomainSuccessfully() {
            Enrollment result = mapper.toDomain(entityEnrollment);

            assertNotNull(result);
            assertEquals(entityEnrollment.getId(), result.getId());
            assertEquals(entityEnrollment.getStatus(), result.getStatus());
            assertEquals(entityEnrollment.getGrade(), result.getGrade());
            assertEquals(entityEnrollment.getEnrollmentDate(), result.getEnrollmentDate());
            assertEquals(entityEnrollment.getCreatedAt(), result.getCreatedAt());
            assertEquals(entityEnrollment.getUpdatedAt(), result.getUpdatedAt());

            assertNotNull(result.getStudent());
            assertEquals(entityEnrollment.getStudent().getId(), result.getStudent().getId());
            assertEquals(entityEnrollment.getStudent().getName(), result.getStudent().getName());
            assertEquals(entityEnrollment.getStudent().getCpf(), result.getStudent().getCpf());

            assertNotNull(result.getSubject());
            assertEquals(entityEnrollment.getSubject().getId(), result.getSubject().getId());
            assertEquals(entityEnrollment.getSubject().getName(), result.getSubject().getName());
            assertEquals(entityEnrollment.getSubject().getCode(), result.getSubject().getCode());
        }

        @Test
        @DisplayName("Should handle null entity enrollment")
        void shouldHandleNullEntityEnrollment() {
            Enrollment result = mapper.toDomain(null);
            assertNull(result);
        }

        @Test
        @DisplayName("Should handle entity enrollment with null grade")
        void shouldHandleEntityEnrollmentWithNullGrade() {
            entityEnrollment.setGrade(null);

            Enrollment result = mapper.toDomain(entityEnrollment);

            assertNotNull(result);
            assertNull(result.getGrade());
            assertEquals(entityEnrollment.getStatus(), result.getStatus());
        }

        @Test
        @DisplayName("Should map list of entity enrollments to domain")
        void shouldMapListOfEntityEnrollmentsToDomain() {
            EnrollmentEntity entity2 = new EnrollmentEntity();
            entity2.setId(2L);
            entity2.setStudent(entityStudent);
            entity2.setSubject(entitySubject);
            entity2.setStatus(EnrollmentStatus.SUSPENDED);
            entity2.setGrade(BigDecimal.valueOf(7.0));

            List<EnrollmentEntity> entityList = Arrays.asList(entityEnrollment, entity2);

            List<Enrollment> result = mapper.toDomainList(entityList);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(entityEnrollment.getId(), result.get(0).getId());
            assertEquals(entity2.getId(), result.get(1).getId());
            assertEquals(EnrollmentStatus.ACTIVE, result.get(0).getStatus());
            assertEquals(EnrollmentStatus.SUSPENDED, result.get(1).getStatus());
        }
    }

    @Nested
    @DisplayName("Enrollment Status Tests")
    class EnrollmentStatusTests {

        @Test
        @DisplayName("Should map all enrollment statuses correctly")
        void shouldMapAllEnrollmentStatusesCorrectly() {
            EnrollmentStatus[] statuses = EnrollmentStatus.values();

            for (EnrollmentStatus status : statuses) {
                domainEnrollment.setStatus(status);
                
                EnrollmentEntity entityResult = mapper.toEntity(domainEnrollment);
                Enrollment domainResult = mapper.toDomain(entityResult);

                assertEquals(status, entityResult.getStatus());
                assertEquals(status, domainResult.getStatus());
            }
        }

        @Test
        @DisplayName("Should handle completed enrollment with completion date")
        void shouldHandleCompletedEnrollmentWithCompletionDate() {
            LocalDateTime completionDate = LocalDateTime.now();
            domainEnrollment.setStatus(EnrollmentStatus.COMPLETED);
            domainEnrollment.setCompletionDate(completionDate);

            EnrollmentEntity entityResult = mapper.toEntity(domainEnrollment);
            Enrollment domainResult = mapper.toDomain(entityResult);

            assertEquals(EnrollmentStatus.COMPLETED, entityResult.getStatus());
            assertEquals(completionDate, entityResult.getCompletionDate());
            assertEquals(EnrollmentStatus.COMPLETED, domainResult.getStatus());
            assertEquals(completionDate, domainResult.getCompletionDate());
        }
    }

    @Nested
    @DisplayName("Grade Tests")
    class GradeTests {

        @Test
        @DisplayName("Should map different grade values correctly")
        void shouldMapDifferentGradeValuesCorrectly() {
            BigDecimal[] grades = {
                BigDecimal.ZERO,
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(7.0),
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(8.75)
            };

            for (BigDecimal grade : grades) {
                domainEnrollment.setGrade(grade);
                
                EnrollmentEntity entityResult = mapper.toEntity(domainEnrollment);
                Enrollment domainResult = mapper.toDomain(entityResult);

                assertEquals(grade, entityResult.getGrade());
                assertEquals(grade, domainResult.getGrade());
            }
        }

        @Test
        @DisplayName("Should preserve grade precision")
        void shouldPreserveGradePrecision() {
            BigDecimal preciseGrade = new BigDecimal("8.567");
            domainEnrollment.setGrade(preciseGrade);

            EnrollmentEntity entityResult = mapper.toEntity(domainEnrollment);
            Enrollment domainResult = mapper.toDomain(entityResult);

            assertEquals(preciseGrade, entityResult.getGrade());
            assertEquals(preciseGrade, domainResult.getGrade());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty lists")
        void shouldHandleEmptyLists() {
            List<Enrollment> emptyDomainList = new ArrayList<>();
            List<EnrollmentEntity> emptyEntityList = new ArrayList<>();

            List<EnrollmentEntity> entityResult = mapper.toEntityList(emptyDomainList);
            List<Enrollment> domainResult = mapper.toDomainList(emptyEntityList);

            assertNotNull(entityResult);
            assertNotNull(domainResult);
            assertTrue(entityResult.isEmpty());
            assertTrue(domainResult.isEmpty());
        }

        @Test
        @DisplayName("Should handle null lists")
        void shouldHandleNullLists() {
            List<EnrollmentEntity> entityResult = mapper.toEntityList(null);
            List<Enrollment> domainResult = mapper.toDomainList(null);

            assertNull(entityResult);
            assertNull(domainResult);
        }

        @Test
        @DisplayName("Should preserve timestamp precision")
        void shouldPreserveTimestampPrecision() {
            LocalDateTime specificTime = LocalDateTime.of(2023, 12, 15, 14, 30, 45, 123456789);
            domainEnrollment.setEnrollmentDate(specificTime);
            domainEnrollment.setCreatedAt(specificTime);
            domainEnrollment.setUpdatedAt(specificTime.plusMinutes(5));

            EnrollmentEntity entityResult = mapper.toEntity(domainEnrollment);
            Enrollment domainResult = mapper.toDomain(entityResult);

            assertEquals(specificTime, entityResult.getEnrollmentDate());
            assertEquals(specificTime, entityResult.getCreatedAt());
            assertEquals(specificTime.plusMinutes(5), entityResult.getUpdatedAt());
            
            assertEquals(specificTime, domainResult.getEnrollmentDate());
            assertEquals(specificTime, domainResult.getCreatedAt());
            assertEquals(specificTime.plusMinutes(5), domainResult.getUpdatedAt());
        }

        @Test
        @DisplayName("Should handle enrollment with minimal required fields")
        void shouldHandleEnrollmentWithMinimalRequiredFields() {
            Enrollment minimalEnrollment = new Enrollment();
            minimalEnrollment.setStudent(domainStudent);
            minimalEnrollment.setSubject(domainSubject);
            minimalEnrollment.setStatus(EnrollmentStatus.ACTIVE);

            EnrollmentEntity result = mapper.toEntity(minimalEnrollment);

            assertNotNull(result);
            assertNotNull(result.getStudent());
            assertNotNull(result.getSubject());
            assertEquals(EnrollmentStatus.ACTIVE, result.getStatus());
            assertNull(result.getGrade());
            assertNull(result.getCompletionDate());
        }
    }
}
