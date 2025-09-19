package br.edu.infnet.classroomapi.application.services;

import br.edu.infnet.classroomapi.application.dto.request.CreateStudentRequestDTO;
import br.edu.infnet.classroomapi.application.dto.request.AddressRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.AddressResponseDTO;
import br.edu.infnet.classroomapi.application.mappers.StudentDTOMapper;
import br.edu.infnet.classroomapi.domain.entities.Student;
import br.edu.infnet.classroomapi.domain.repositories.StudentRepository;
import br.edu.infnet.classroomapi.domain.entities.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("StudentApplicationService Tests")
class StudentApplicationServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentDTOMapper studentMapper;

    @InjectMocks
    private StudentApplicationService studentService;

    private CreateStudentRequestDTO createStudentRequest;
    private CreateStudentRequestDTO updateStudentRequest;
    private Student student;
    private StudentResponseDTO studentResponseDTO;

    @BeforeEach
    void setUp() {
        AddressRequestDTO addressRequest = new AddressRequestDTO(
                "Rua das Flores", "123", "Apto 45", "Centro", "São Paulo", "SP", "01234-567", "Brasil"
        );
        createStudentRequest = new CreateStudentRequestDTO(
                "João Silva", "12345678901", "joao@email.com", "(11) 99999-9999", addressRequest
        );

        AddressRequestDTO updateAddressRequest = new AddressRequestDTO(
                "Rua das Rosas", "456", null, "Bela Vista", "São Paulo", "SP", "07890-123", "Brasil"
        );
        updateStudentRequest = new CreateStudentRequestDTO(
                "João Silva Santos", "12345678901", "joao.santos@email.com", "(11) 88888-8888", updateAddressRequest
        );

        String cpf = "12345678901";
        Address address = new Address("Rua das Flores", "123", "Apto 45", "Centro", "São Paulo", "SP", "01234-567", "Brasil");
        student = new Student("João Silva", cpf, "joao@email.com", "(11) 99999-9999", address);
        student.setId(1L);

        AddressResponseDTO addressResponseDTO = AddressResponseDTO.builder()
                .street("Rua das Flores")
                .number("123")
                .complement("Apto 45")
                .neighborhood("Centro")
                .city("São Paulo")
                .state("SP")
                .zipCode("01234-567")
                .country("Brasil")
                .build();
        studentResponseDTO = StudentResponseDTO.builder()
                .id(1L)
                .name("João Silva")
                .cpf("12345678901")
                .email("joao@email.com")
                .phone("(11) 99999-9999")
                .address(addressResponseDTO)
                .build();
    }

    @Nested
    @DisplayName("Create Student Tests")
    class CreateStudentTests {

        @Test
        @DisplayName("Should create student successfully")
        void shouldCreateStudentSuccessfully() {
            when(studentRepository.existsByCpf(anyString())).thenReturn(false);
            when(studentRepository.existsByEmail(anyString())).thenReturn(false);
            when(studentMapper.toDomain(createStudentRequest)).thenReturn(student);
            when(studentRepository.save(any(Student.class))).thenReturn(student);
            when(studentMapper.toResponseDTO(student)).thenReturn(studentResponseDTO);

            StudentResponseDTO result = studentService.createStudent(createStudentRequest);

            assertNotNull(result);
            assertEquals(studentResponseDTO.getId(), result.getId());
            assertEquals(studentResponseDTO.getName(), result.getName());
            assertEquals(studentResponseDTO.getCpf(), result.getCpf());
            assertEquals(studentResponseDTO.getEmail(), result.getEmail());

            verify(studentRepository).existsByCpf("12345678901");
            verify(studentRepository).existsByEmail("joao@email.com");
            verify(studentMapper).toDomain(createStudentRequest);
            verify(studentRepository).save(student);
            verify(studentMapper).toResponseDTO(student);
        }

        @Test
        @DisplayName("Should throw exception when CPF already exists")
        void shouldThrowExceptionWhenCPFAlreadyExists() {
            when(studentRepository.existsByCpf(anyString())).thenReturn(true);

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.createStudent(createStudentRequest)
            );

            assertEquals("CPF already exists", exception.getMessage());
            verify(studentRepository).existsByCpf("12345678901");
            verify(studentRepository, never()).existsByEmail(anyString());
            verify(studentRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            when(studentRepository.existsByCpf(anyString())).thenReturn(false);
            when(studentRepository.existsByEmail(anyString())).thenReturn(true);

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.createStudent(createStudentRequest)
            );

            assertEquals("Email already exists", exception.getMessage());
            verify(studentRepository).existsByCpf("12345678901");
            verify(studentRepository).existsByEmail("joao@email.com");
            verify(studentRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Find Student Tests")
    class FindStudentTests {

        @Test
        @DisplayName("Should find student by ID successfully")
        void shouldFindStudentByIdSuccessfully() {
            when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
            when(studentMapper.toResponseDTO(student)).thenReturn(studentResponseDTO);

            StudentResponseDTO result = studentService.findById(1L);

            assertNotNull(result);
            assertEquals(studentResponseDTO.getId(), result.getId());
            assertEquals(studentResponseDTO.getName(), result.getName());

            verify(studentRepository).findById(1L);
            verify(studentMapper).toResponseDTO(student);
        }

        @Test
        @DisplayName("Should throw exception when student not found by ID")
        void shouldThrowExceptionWhenStudentNotFoundById() {
            when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> studentService.findById(1L)
            );

            assertEquals("Student not found with id: 1", exception.getMessage());
            verify(studentRepository).findById(1L);
            verify(studentMapper, never()).toResponseDTO(any());
        }

        @Test
        @DisplayName("Should find student by CPF successfully")
        void shouldFindStudentByCPFSuccessfully() {
            when(studentRepository.findByCpf(anyString())).thenReturn(Optional.of(student));
            when(studentMapper.toResponseDTO(student)).thenReturn(studentResponseDTO);

            StudentResponseDTO result = studentService.findByCpf("12345678901");

            assertNotNull(result);
            assertEquals(studentResponseDTO.getCpf(), result.getCpf());

            verify(studentRepository).findByCpf("12345678901");
            verify(studentMapper).toResponseDTO(student);
        }

        @Test
        @DisplayName("Should find all students successfully")
        void shouldFindAllStudentsSuccessfully() {
            List<Student> students = Arrays.asList(student);
            List<StudentResponseDTO> responseDTOs = Arrays.asList(studentResponseDTO);

            when(studentRepository.findAll()).thenReturn(students);
            when(studentMapper.toResponseDTOList(students)).thenReturn(responseDTOs);

            List<StudentResponseDTO> result = studentService.findAll();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(studentResponseDTO.getId(), result.get(0).getId());

            verify(studentRepository).findAll();
            verify(studentMapper).toResponseDTOList(students);
        }

        @Test
        @DisplayName("Should find students by name successfully")
        void shouldFindStudentsByNameSuccessfully() {
            List<Student> students = Arrays.asList(student);
            List<StudentResponseDTO> responseDTOs = Arrays.asList(studentResponseDTO);

            when(studentRepository.findByNameContainingIgnoreCase("João")).thenReturn(students);
            when(studentMapper.toResponseDTOList(students)).thenReturn(responseDTOs);

            List<StudentResponseDTO> result = studentService.findByName("João");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("João Silva", result.get(0).getName());

            verify(studentRepository).findByNameContainingIgnoreCase("João");
            verify(studentMapper).toResponseDTOList(students);
        }
    }

    @Nested
    @DisplayName("Update Student Tests")
    class UpdateStudentTests {

        @Test
        @DisplayName("Should update student successfully")
        void shouldUpdateStudentSuccessfully() {
            Student updatedStudent = new Student(
                "João Silva Santos",
                student.getCpf(),
                "joao.santos@email.com",
                "(11) 88888-8888",
                new Address("Rua das Rosas", "456", null, "Bela Vista", "São Paulo", "SP", "07890-123", "Brasil")
            );
            updatedStudent.setId(1L);

            AddressResponseDTO updatedAddressResponseDTO = AddressResponseDTO.builder()
                    .street("Rua das Rosas")
                    .number("456")
                    .complement(null)
                    .neighborhood("Bela Vista")
                    .city("São Paulo")
                    .state("SP")
                    .zipCode("07890-123")
                    .country("Brasil")
                    .build();
            StudentResponseDTO updatedResponseDTO = StudentResponseDTO.builder()
                    .id(1L)
                    .name("João Silva Santos")
                    .cpf("12345678901")
                    .email("joao.santos@email.com")
                    .phone("(11) 88888-8888")
                    .address(updatedAddressResponseDTO)
                    .build();

            when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
            when(studentRepository.existsByCpf(anyString())).thenReturn(false);
            when(studentRepository.existsByEmail(anyString())).thenReturn(false);
            when(studentMapper.toDomain(updateStudentRequest)).thenReturn(updatedStudent);
            when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);
            when(studentMapper.toResponseDTO(updatedStudent)).thenReturn(updatedResponseDTO);

            StudentResponseDTO result = studentService.updateStudent(1L, updateStudentRequest);

            assertNotNull(result);
            assertEquals("João Silva Santos", result.getName());
            assertEquals("joao.santos@email.com", result.getEmail());

            verify(studentRepository).findById(1L);
            verify(studentRepository).existsByEmail("joao.santos@email.com");
            verify(studentMapper).toDomain(updateStudentRequest);
            verify(studentRepository).save(updatedStudent);
            verify(studentMapper).toResponseDTO(updatedStudent);
        }

        @Test
        @DisplayName("Should throw exception when updating non-existent student")
        void shouldThrowExceptionWhenUpdatingNonExistentStudent() {
            when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> studentService.updateStudent(1L, updateStudentRequest)
            );

            assertEquals("Student not found with id: 1", exception.getMessage());
            verify(studentRepository).findById(1L);
            verify(studentRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when CPF already exists for update")
        void shouldThrowExceptionWhenCPFAlreadyExistsForUpdate() {
            AddressRequestDTO differentAddressRequest = new AddressRequestDTO(
                    "Rua das Rosas", "456", null, "Bela Vista", "São Paulo", "SP", "07890-123", "Brasil"
            );
            CreateStudentRequestDTO differentCpfRequest = new CreateStudentRequestDTO(
                    "João Silva Santos", "98765432100", "joao.santos@email.com", "(11) 88888-8888", differentAddressRequest
            );

            when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
            when(studentRepository.existsByCpf("98765432100")).thenReturn(true);

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.updateStudent(1L, differentCpfRequest)
            );

            assertEquals("CPF already exists", exception.getMessage());
            verify(studentRepository).findById(1L);
            verify(studentRepository).existsByCpf("98765432100");
            verify(studentRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Delete Student Tests")
    class DeleteStudentTests {

        @Test
        @DisplayName("Should delete student successfully")
        void shouldDeleteStudentSuccessfully() {
            when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

            assertDoesNotThrow(() -> studentService.deleteById(1L));

            verify(studentRepository).findById(1L);
            verify(studentRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent student")
        void shouldThrowExceptionWhenDeletingNonExistentStudent() {
            when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> studentService.deleteById(1L)
            );

            assertEquals("Student not found with id: 1", exception.getMessage());
            verify(studentRepository).findById(1L);
            verify(studentRepository, never()).deleteById(anyLong());
        }
    }
}