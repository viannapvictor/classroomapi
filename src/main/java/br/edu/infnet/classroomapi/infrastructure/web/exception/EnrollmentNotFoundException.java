package br.edu.infnet.classroomapi.infrastructure.web.exception;

public class EnrollmentNotFoundException extends RuntimeException {

    public EnrollmentNotFoundException(String message) {
        super(message);
    }

    public EnrollmentNotFoundException(Long id) {
        super("Enrollment not found with id: " + id);
    }
}