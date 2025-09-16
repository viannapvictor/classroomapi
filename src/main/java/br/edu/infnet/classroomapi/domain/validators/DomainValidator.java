package br.edu.infnet.classroomapi.domain.validators;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DomainValidator {
    
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
    
    public static void validateNotEmpty(String value, String fieldName) {
        validateNotNull(value, fieldName);
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
    }
    
    public static void validateEmail(String email) {
        validateNotEmpty(email, "Email");
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    public static void validatePositive(Integer value, String fieldName) {
        validateNotNull(value, fieldName);
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
    }
}