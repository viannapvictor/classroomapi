package br.edu.infnet.classroomapi.infrastructure.web.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }

    public StudentNotFoundException(String field, String value) {
        super("Student not found with " + field + ": " + value);
    }
}