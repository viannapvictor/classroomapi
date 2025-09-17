package br.edu.infnet.classroomapi.infrastructure.web.exception;

public class SubjectNotFoundException extends RuntimeException {

    public SubjectNotFoundException(String message) {
        super(message);
    }

    public SubjectNotFoundException(Long id) {
        super("Subject not found with id: " + id);
    }

    public SubjectNotFoundException(String field, String value) {
        super("Subject not found with " + field + ": " + value);
    }
}