package br.edu.infnet.classroomapi.application.services;

import br.edu.infnet.classroomapi.application.dto.request.CreateStudentRequestDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentResponseDTO;
import br.edu.infnet.classroomapi.application.dto.response.StudentSummaryDTO;
import br.edu.infnet.classroomapi.application.mappers.StudentDTOMapper;
import br.edu.infnet.classroomapi.domain.entities.Student;
import br.edu.infnet.classroomapi.domain.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentApplicationService {

    private final StudentRepository studentRepository;
    private final StudentDTOMapper studentMapper;

    public StudentResponseDTO createStudent(CreateStudentRequestDTO request) {
        if (studentRepository.existsByCpf(request.getCpf())) {
            throw new IllegalArgumentException("CPF already exists");
        }

        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Student student = studentMapper.toDomain(request);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(savedStudent);
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        return studentMapper.toResponseDTO(student);
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO findByCpf(String cpf) {
        Student student = studentRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Student not found with CPF: " + cpf));

        return studentMapper.toResponseDTO(student);
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO findByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found with email: " + email));

        return studentMapper.toResponseDTO(student);
    }

    @Transactional(readOnly = true)
    public List<StudentResponseDTO> findAll() {
        List<Student> students = studentRepository.findAll();
        return studentMapper.toResponseDTOList(students);
    }

    @Transactional(readOnly = true)
    public List<StudentSummaryDTO> findAllSummary() {
        List<Student> students = studentRepository.findAll();
        return studentMapper.toSummaryDTOList(students);
    }

    @Transactional(readOnly = true)
    public List<StudentResponseDTO> findByName(String name) {
        List<Student> students = studentRepository.findByNameContainingIgnoreCase(name);
        return studentMapper.toResponseDTOList(students);
    }

    @Transactional(readOnly = true)
    public List<StudentSummaryDTO> findBySubjectId(Long subjectId) {
        List<Student> students = studentRepository.findBySubjectId(subjectId);
        return studentMapper.toSummaryDTOList(students);
    }

    public StudentResponseDTO updateStudent(Long id, CreateStudentRequestDTO request) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        if (!existingStudent.getCpf().equals(request.getCpf()) &&
            studentRepository.existsByCpf(request.getCpf())) {
            throw new IllegalArgumentException("CPF already exists");
        }

        if (!existingStudent.getEmail().equals(request.getEmail()) &&
            studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Student updatedStudent = studentMapper.toDomain(request);
        updatedStudent.setId(id);
        updatedStudent.setCreatedAt(existingStudent.getCreatedAt());

        Student savedStudent = studentRepository.save(updatedStudent);
        return studentMapper.toResponseDTO(savedStudent);
    }

    public void deleteById(Long id) {
        if (!studentRepository.findById(id).isPresent()) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}