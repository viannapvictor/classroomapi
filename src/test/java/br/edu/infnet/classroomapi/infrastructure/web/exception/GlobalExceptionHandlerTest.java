package br.edu.infnet.classroomapi.infrastructure.web.exception;

import br.edu.infnet.classroomapi.infrastructure.web.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Nested
    @DisplayName("Runtime Exception Tests")
    class RuntimeExceptionTests {

        @Test
        @DisplayName("Should handle RuntimeException with message")
        void shouldHandleRuntimeExceptionWithMessage() {
            String errorMessage = "Something went wrong";
            RuntimeException exception = new RuntimeException(errorMessage);

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleRuntimeException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            
            ApiResponse<Void> body = response.getBody();
            assertNotNull(body);
            assertFalse(body.isSuccess());
            assertNull(body.getData());
            assertNotNull(body.getError());
            assertEquals("INTERNAL_ERROR", body.getError().getCode());
            assertEquals(errorMessage, body.getError().getMessage());
        }

        @Test
        @DisplayName("Should handle RuntimeException with null message")
        void shouldHandleRuntimeExceptionWithNullMessage() {
            RuntimeException exception = new RuntimeException((String) null);

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleRuntimeException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            
            ApiResponse<Void> body = response.getBody();
            assertNotNull(body);
            assertFalse(body.isSuccess());
            assertEquals("INTERNAL_ERROR", body.getError().getCode());
            assertNull(body.getError().getMessage());
        }

        @Test
        @DisplayName("Should handle RuntimeException subclasses")
        void shouldHandleRuntimeExceptionSubclasses() {
            IllegalStateException exception = new IllegalStateException("Invalid state");

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleRuntimeException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            
            ApiResponse<Void> body = response.getBody();
            assertNotNull(body);
            assertFalse(body.isSuccess());
            assertEquals("INTERNAL_ERROR", body.getError().getCode());
            assertEquals("Invalid state", body.getError().getMessage());
        }
    }

    @Nested
    @DisplayName("Illegal Argument Exception Tests")
    class IllegalArgumentExceptionTests {

        @Test
        @DisplayName("Should handle IllegalArgumentException with message")
        void shouldHandleIllegalArgumentExceptionWithMessage() {
            String errorMessage = "Invalid argument provided";
            IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleIllegalArgumentException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            
            ApiResponse<Void> body = response.getBody();
            assertNotNull(body);
            assertFalse(body.isSuccess());
            assertNull(body.getData());
            assertNotNull(body.getError());
            assertEquals("BAD_REQUEST", body.getError().getCode());
            assertEquals(errorMessage, body.getError().getMessage());
        }

        @Test
        @DisplayName("Should handle IllegalArgumentException with null message")
        void shouldHandleIllegalArgumentExceptionWithNullMessage() {
            IllegalArgumentException exception = new IllegalArgumentException((String) null);

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleIllegalArgumentException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            
            ApiResponse<Void> body = response.getBody();
            assertNotNull(body);
            assertFalse(body.isSuccess());
            assertEquals("BAD_REQUEST", body.getError().getCode());
            assertNull(body.getError().getMessage());
        }

        @Test
        @DisplayName("Should handle different IllegalArgumentException scenarios")
        void shouldHandleDifferentIllegalArgumentExceptionScenarios() {
            String[] errorMessages = {
                "Name cannot be null",
                "Email already exists",
                "CPF format is invalid",
                "Grade must be between 0 and 10"
            };

            for (String message : errorMessages) {
                IllegalArgumentException exception = new IllegalArgumentException(message);
                ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleIllegalArgumentException(exception);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals(message, response.getBody().getError().getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Method Argument Not Valid Exception Tests")
    class MethodArgumentNotValidExceptionTests {

        @Test
        @DisplayName("Should handle MethodArgumentNotValidException with field errors")
        void shouldHandleMethodArgumentNotValidExceptionWithFieldErrors() {
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError1 = new FieldError("student", "name", "Name is required");
            FieldError fieldError2 = new FieldError("student", "email", "Email format is invalid");
            
            when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));
            
            MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<ApiResponse<Map<String, String>>> response = exceptionHandler.handleValidationExceptions(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            
            ApiResponse<Map<String, String>> body = response.getBody();
            assertNotNull(body);
            assertFalse(body.isSuccess());
            assertNotNull(body.getData());
            
            Map<String, String> validationErrors = body.getData();
            assertEquals(2, validationErrors.size());
            assertEquals("Name is required", validationErrors.get("name"));
            assertEquals("Email format is invalid", validationErrors.get("email"));
            
            assertNotNull(body.getError());
            assertEquals("VALIDATION_ERROR", body.getError().getCode());
            assertEquals("Invalid input data", body.getError().getMessage());
        }

        @Test
        @DisplayName("Should handle MethodArgumentNotValidException with null error messages")
        void shouldHandleMethodArgumentNotValidExceptionWithNullErrorMessages() {
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError = new FieldError("student", "name", null);
            
            when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError));
            
            MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<ApiResponse<Map<String, String>>> response = exceptionHandler.handleValidationExceptions(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            
            Map<String, String> validationErrors = response.getBody().getData();
            assertEquals("Invalid value", validationErrors.get("name"));
        }

        @Test
        @DisplayName("Should handle MethodArgumentNotValidException with duplicate field names")
        void shouldHandleMethodArgumentNotValidExceptionWithDuplicateFieldNames() {
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError1 = new FieldError("student", "email", "Email is required");
            FieldError fieldError2 = new FieldError("student", "email", "Email format is invalid");
            
            when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));
            
            MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<ApiResponse<Map<String, String>>> response = exceptionHandler.handleValidationExceptions(exception);

            Map<String, String> validationErrors = response.getBody().getData();
            assertEquals("Email is required", validationErrors.get("email"));
            assertEquals(1, validationErrors.size());
        }

        @Test
        @DisplayName("Should handle MethodArgumentNotValidException with empty field errors")
        void shouldHandleMethodArgumentNotValidExceptionWithEmptyFieldErrors() {
            BindingResult bindingResult = mock(BindingResult.class);
            when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList());
            
            MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<ApiResponse<Map<String, String>>> response = exceptionHandler.handleValidationExceptions(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            
            Map<String, String> validationErrors = response.getBody().getData();
            assertTrue(validationErrors.isEmpty());
        }
    }

    @Nested
    @DisplayName("Custom Exception Tests")
    class CustomExceptionTests {

        @Test
        @DisplayName("Should handle StudentNotFoundException")
        void shouldHandleStudentNotFoundException() {
            StudentNotFoundException exception = new StudentNotFoundException("Student not found with id: 1");

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleStudentNotFoundException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("STUDENT_NOT_FOUND", response.getBody().getError().getCode());
            assertEquals("Student not found with id: 1", response.getBody().getError().getMessage());
        }

        @Test
        @DisplayName("Should handle SubjectNotFoundException")
        void shouldHandleSubjectNotFoundException() {
            SubjectNotFoundException exception = new SubjectNotFoundException("Subject not found with code: JAVA101");

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleSubjectNotFoundException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("SUBJECT_NOT_FOUND", response.getBody().getError().getCode());
            assertEquals("Subject not found with code: JAVA101", response.getBody().getError().getMessage());
        }

        @Test
        @DisplayName("Should handle EnrollmentNotFoundException")
        void shouldHandleEnrollmentNotFoundException() {
            EnrollmentNotFoundException exception = new EnrollmentNotFoundException("Enrollment not found with id: 1");

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleEnrollmentNotFoundException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("ENROLLMENT_NOT_FOUND", response.getBody().getError().getCode());
            assertEquals("Enrollment not found with id: 1", response.getBody().getError().getMessage());
        }

        @Test
        @DisplayName("Should handle DuplicateResourceException")
        void shouldHandleDuplicateResourceException() {
            DuplicateResourceException exception = new DuplicateResourceException("Email already exists");

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleDuplicateResourceException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertEquals("DUPLICATE_RESOURCE", response.getBody().getError().getCode());
            assertEquals("Email already exists", response.getBody().getError().getMessage());
        }

        @Test
        @DisplayName("Should handle UnauthorizedOperationException")
        void shouldHandleUnauthorizedOperationException() {
            UnauthorizedOperationException exception = new UnauthorizedOperationException("You can only access your own resources");

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleUnauthorizedOperationException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertEquals("FORBIDDEN", response.getBody().getError().getCode());
            assertEquals("You can only access your own resources", response.getBody().getError().getMessage());
        }

        @Test
        @DisplayName("Should handle generic Exception")
        void shouldHandleGenericException() {
            Exception exception = new Exception("Unexpected error occurred");

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleGenericException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("INTERNAL_ERROR", response.getBody().getError().getCode());
            assertEquals("An unexpected error occurred", response.getBody().getError().getMessage());
        }
    }

    @Nested
    @DisplayName("Response Structure Tests")
    class ResponseStructureTests {

        @Test
        @DisplayName("Should return consistent response structure for RuntimeException")
        void shouldReturnConsistentResponseStructureForRuntimeException() {
            RuntimeException exception = new RuntimeException("Test error");

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleRuntimeException(exception);

            ApiResponse<Void> body = response.getBody();
            assertNotNull(body);
            assertFalse(body.isSuccess());
            assertNull(body.getData());
            assertNull(body.getMessage());
            assertNotNull(body.getError());
            assertEquals("INTERNAL_ERROR", body.getError().getCode());
            assertEquals("Test error", body.getError().getMessage());
        }

        @Test
        @DisplayName("Should return consistent response structure for IllegalArgumentException")
        void shouldReturnConsistentResponseStructureForIllegalArgumentException() {
            IllegalArgumentException exception = new IllegalArgumentException("Bad request");

            ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleIllegalArgumentException(exception);

            ApiResponse<Void> body = response.getBody();
            assertNotNull(body);
            assertFalse(body.isSuccess());
            assertNull(body.getData());
            assertNull(body.getMessage());
            assertNotNull(body.getError());
            assertEquals("BAD_REQUEST", body.getError().getCode());
            assertEquals("Bad request", body.getError().getMessage());
        }

        @Test
        @DisplayName("Should return consistent response structure for MethodArgumentNotValidException")
        void shouldReturnConsistentResponseStructureForMethodArgumentNotValidException() {
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError = new FieldError("object", "field", "error message");
            when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError));
            
            MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<ApiResponse<Map<String, String>>> response = exceptionHandler.handleValidationExceptions(exception);

            ApiResponse<Map<String, String>> body = response.getBody();
            assertNotNull(body);
            assertFalse(body.isSuccess());
            assertNotNull(body.getData());
            assertNull(body.getMessage());
            assertNotNull(body.getError());
            assertEquals("VALIDATION_ERROR", body.getError().getCode());
            assertEquals("Invalid input data", body.getError().getMessage());
        }
    }
}
