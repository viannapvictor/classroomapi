package br.edu.infnet.classroomapi.infrastructure.web.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String field, String value) {
        super(field + " '" + value + "' already exists");
    }
}