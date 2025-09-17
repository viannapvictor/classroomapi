package br.edu.infnet.classroomapi.infrastructure.web.exception;

public class UnauthorizedOperationException extends RuntimeException {

    public UnauthorizedOperationException(String message) {
        super(message);
    }

    public UnauthorizedOperationException() {
        super("You are not authorized to perform this operation");
    }
}